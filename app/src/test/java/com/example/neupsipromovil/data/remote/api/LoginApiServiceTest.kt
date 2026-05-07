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
}
