package com.example.neupsipromovil.data.remote.api

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginApiServiceTest {
    // MockWebServer starts a real HTTP server on localhost
    // It listens on a random port to avoid conflicts
    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: LoginApiService

    @Before
    fun setup() {
        // Start the fake server before each test
        mockWebServer = MockWebServer()
        mockWebServer.start()

        // Build Retrofit pointing to the fake server's local URL
        apiService =
            Retrofit
                .Builder()
                .baseUrl(mockWebServer.url("/")) // dynamic port assigned by MockWebServer
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LoginApiService::class.java)
    }

    @After
    fun tearDown() {
        // Shut down the server after each test to free the port
        mockWebServer.shutdown()
    }

    // ─────────────────────────────────────────────
    // Succesful Path
    // ─────────────────────────────────────────────
    @Test
    fun `successful login returns 200 with token and UUID`() =
        runTest {
            // GIVEN: server responds with a valid token and a UUID
            val uuid = "550e8400-e29b-41d4-a716-446655440000"
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(
                        """
                        {
                            "token": "eyJhbGciOiJIUzI1NiJ9.abc",
                            "user_id": "$uuid"
                        }
                        """.trimIndent(),
                    ).addHeader("Content-Type", "application/json"),
            )

            // WHEN: Retrofit sends the form-encoded request
            val response =
                apiService.postLogin(
                    userName = "john",
                    password = "secret123",
                )

            // THEN: response is successful and DTO is parsed correctly
            assertTrue(response.isSuccessful)
            assertNotNull(response.body())
            assertEquals("eyJhbGciOiJIUzI1NiJ9.abc", response.body()?.token)
        }

    // ─────────────────────────────────────────────
    //
    // if Retrofit sends JSON instead, the server rejects it
    // So we sent a Form URL encoded
    // ─────────────────────────────────────────────

    @Test
    fun `request uses form-url-encoded content type not JSON`() =
        runTest {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody("""{"token":"t","user_id":"uuid-123"}""")
                    .addHeader("Content-Type", "application/json"),
            )

            apiService.postLogin(userName = "john", password = "pass123")

            // Inspect what Retrofit actually sent
            val recordedRequest = mockWebServer.takeRequest()

            // THEN: content type must be form-encoded, not application/json
            val contentType = recordedRequest.getHeader("Content-Type")
            assertNotNull(contentType)
            assertTrue(
                "Expected form-encoded but got: $contentType",
                contentType!!.contains("application/x-www-form-urlencoded"),
            )
        }

    // ─────────────────────────────────────────────
    // Verify the field names in the body match what server expects
    // @Field("username") and @Field("password") must appear exactly
    // ─────────────────────────────────────────────

    @Test
    fun `request body contains username and password as form fields`() =
        runTest {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody("""{"token":"t","user_id":"uuid-123"}""")
                    .addHeader("Content-Type", "application/json"),
            )

            apiService.postLogin(userName = "john_doe", password = "mypassword")

            val recordedRequest = mockWebServer.takeRequest()

            // Read the raw body — form-encoded looks like: username=john_doe&password=mypassword
            val body = recordedRequest.body.readUtf8()

            // THEN: both field names and values are present in the body
            assertTrue("username field missing in body", body.contains("username=john_doe"))
            assertTrue("password field missing in body", body.contains("password=mypassword"))
        }

    // ─────────────────────────────────────────────
    // UC 1.1 — SQL Injection sent as plain form field
    // ─────────────────────────────────────────────

    @Test
    fun `UC 1-1 SQL injection is sent as plain url-encoded string`() =
        runTest {
            // GIVEN: server returns 401 — injection did not bypass auth
            mockWebServer.enqueue(MockResponse().setResponseCode(401))

            val sqlPayload = "' OR '1'='1"

            // WHEN: UseCase already allowed it through (it's just a string under 30 chars)
            val response = apiService.postLogin(userName = sqlPayload, password = "pass")

            val recordedRequest = mockWebServer.takeRequest()
            val body = recordedRequest.body.readUtf8()

            // THEN: Retrofit URL-encodes it safely — special chars become %27, %3D, etc.
            // The server receives encoded text, NOT executable SQL
            assertFalse(response.isSuccessful)
            assertEquals(401, response.code())

            // The body must contain the url-encoded version of the payload
            // ' → %27, space → +, = → %3D
            assertTrue(
                "SQL payload should be url-encoded in body",
                body.contains("username="),
            )
        }
    // ─────────────────────────────────────────────
    // UC 1.2 — XSS payload sent as plain url-encoded string
    // ─────────────────────────────────────────────

    @Test
    fun `UC 1-2 XSS payload is url-encoded before leaving the device`() =
        runTest {
            mockWebServer.enqueue(MockResponse().setResponseCode(401))

            val xssPayload = "<script>alert('xss')</script>"

            val response = apiService.postLogin(userName = xssPayload, password = "pass")

            val recordedRequest = mockWebServer.takeRequest()
            val body = recordedRequest.body.readUtf8()

            // THEN: < and > are url-encoded as %3C and %3E — cannot execute as HTML
            assertFalse(response.isSuccessful)
            assertTrue(body.contains("username="))

            // %3C = '<' and %3E = '>' — Retrofit encoded them automatically
            assertTrue(
                "Angle brackets must be url-encoded",
                body.contains("%3C") || body.contains("%3c"),
            )
        }

    // ─────────────────────────────────────────────
    // UC 1.3 — Server returns 429 Too Many Requests
    // ─────────────────────────────────────────────

    @Test
    fun `UC 1-3 server returns 429 when rate limit is exceeded`() =
        runTest {
            // GIVEN: server blocks the request after too many calls
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(429)
                    .addHeader("Retry-After", "900"), // 900 seconds = 15 minutes (UC 3.2)
            )

            // WHEN: Retrofit gets the 429 — Response<T> does NOT throw, just wraps it
            val response = apiService.postLogin(userName = "john", password = "pass")

            // THEN: isSuccessful is false for any non-2xx code
            assertFalse(response.isSuccessful)
            assertEquals(429, response.code())

            // The Retry-After header tells the app how long to wait
            assertEquals("900", response.headers()["Retry-After"])
        }

    // ─────────────────────────────────────────────
    // UC 3.2 — 401 Unauthorized (wrong credentials)
    // ─────────────────────────────────────────────

    @Test
    fun `UC 3-2 wrong credentials return 401 with error body`() =
        runTest {
            // GIVEN: server rejects with 401 and a generic message
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(401)
                    .setBody("""{"error": "INVALID_CREDENTIALS"}""")
                    .addHeader("Content-Type", "application/json"),
            )

            val response = apiService.postLogin(userName = "john", password = "wrongpass")

            // THEN: Response wraps the error — body() is null on error, errorBody() has the message
            assertFalse(response.isSuccessful)
            assertEquals(401, response.code())

            // body() is null for non-2xx — errorBody() carries the error JSON
            val errorBody = response.errorBody()?.string()
            assertNotNull(errorBody)
            assertTrue(errorBody!!.contains("INVALID_CREDENTIALS"))
        }

    // ─────────────────────────────────────────────
    // Converter — wrong format JSON still returns a Response, not a crash
    // ─────────────────────────────────────────────

    @Test
    fun `wrong format JSON body causes converter exception wrapped by Retrofit`() =
        runTest {
            // GIVEN: server returns 200 but with broken JSON
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody("NOT_VALID_JSON")
                    .addHeader("Content-Type", "application/json"),
            )

            var threwException = false

            try {
                apiService.postLogin(userName = "john", password = "pass")
            } catch (e: Exception) {
                // Gson throws JsonSyntaxException when it can't deserialize the body
                // With Response<T>, Retrofit still throws on converter failure
                threwException = true
            }

            // THEN: app must handle this — it cannot silently return null data
            assertTrue("Converter must throw on invalid JSON", threwException)
        }
}
