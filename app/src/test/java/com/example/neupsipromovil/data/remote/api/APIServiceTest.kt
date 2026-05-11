package com.example.neupsipromovil.data.remote.api

import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIServiceTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: APIService

    @Before
    fun setup() {
        // Wakes up a fake http server
        mockWebServer = MockWebServer()
        mockWebServer.start()

        // OkHttpClient without an auth interceptor — we test raw HTTP behavior
        val client = OkHttpClient.Builder().build()

        apiService =
            Retrofit
                .Builder()
                // Fake Server
                .baseUrl(mockWebServer.url("/"))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIService::class.java)
    }

    // Shut down server after each tests
    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    // ─────────────────────────────────────────────
    // Happy path — 200 with full profile JSON
    // ─────────────────────────────────────────────

    @Test
    fun `successful profile fetch returns 200 and parses all fields`() =
        runTest {
            // GIVEN: server returns a complete profile matching UserProfileResponse structure
            val uuid = "550e8400-e29b-41d4-a716-446655440000"
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(
                        """
                        {
                            "success": true,
                            "data": {
                                "personalInfo": {
                                    "fullName": "John Doe",
                                    "profilePhoto": "https://cdn.example.com/avatar.jpg",
                                    "birthDate": "1994-05-15",
                                    "age": 30
                                },
                                "clinicalInfo": {
                                    "unitEntryDate": "2024-01-15",
                                    "neuroEntryDate": "2024-01-20",
                                    "neuroStatus": "Active",
                                    "protocol": "P001",
                                    "state": "Active",
                                    "stage": "Stand By",
                                    "prosthetist": "Dr. Pedro"
                                },
                                "assignment": {
                                    "relationId": "rel123",
                                    "assignedClinic": "maria"
                                },
                                "nextAppointment": {
                                    "date": "2024-06-15",
                                    "time": "10:00 AM"
                                }
                            }
                        }
                        """.trimIndent(),
                    ).addHeader("Content-Type", "application/json"),
            )

            // Save all the request for verification
            val response = apiService.getUserProfile(uuid)

            // THEN: UserProfileResponse is parsed correctly
            assertTrue(response.success)
            assertNotNull(response.data)

            // THEN: PersonalInfo fields parsed correctly
            assertEquals("John Doe", response.data.personalInfo.fullName)
            assertEquals("https://cdn.example.com/avatar.jpg", response.data.personalInfo.profilePhoto)
            assertEquals("1994-05-15", response.data.personalInfo.birthDate)
            assertEquals(30, response.data.personalInfo.age)

            // THEN: ClinicalInfo fields parsed correctly
            assertEquals("2024-01-15", response.data.clinicalInfo.unitEntryDate)
            assertEquals("2024-01-20", response.data.clinicalInfo.neuroEntryDate)
            assertEquals("Active", response.data.clinicalInfo.neuroStatus)
            assertEquals("P001", response.data.clinicalInfo.protocol)
            assertEquals("Stand By", response.data.clinicalInfo.stage)
            assertEquals("Dr. Pedro", response.data.clinicalInfo.prosthetist)

            // THEN: Assignment fields parsed correctly
            assertEquals("rel123", response.data.assignment.relationId)
            assertEquals("maria", response.data.assignment.assignedClinic)

            // THEN: NextAppointment fields parsed correctly
            assertEquals("2024-06-15", response.data.nextAppointment?.date)
            assertEquals("10:00 AM", response.data.nextAppointment?.time)
        }
    // ─────────────────────────────────────────────
    // Verify the request hits the correct path
    // ─────────────────────────────────────────────

    @Test
    fun `request path includes userId as path parameter`() =
        runTest {
            val uuid = "550e8400-e29b-41d4-a716-446655440000"
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody("""{"user_id":"$uuid","username":"john","email":"j@j.com","full_name":"John","avatar_url":null}""")
                    .addHeader("Content-Type", "application/json"),
            )

            apiService.getUserProfile(uuid)

            val recordedRequest = mockWebServer.takeRequest()

            // THEN: Retrofit correctly interpolated the UUID into the path
            assertEquals("/api/profile/$uuid", recordedRequest.path)
            assertEquals("GET", recordedRequest.method)
        }

    // ─────────────────────────────────────────────
    // UC 1.1 — No authentication — server returns 401
    // ─────────────────────────────────────────────

    @Test
    fun `UC 1-1 request without token causes server to return 401`() =
        runTest {
            // GIVEN: server rejects because no Authorization header was sent
            mockWebServer.enqueue(MockResponse().setResponseCode(401))

            var statusCode = 0
            try {
                // APIService uses @GET without Response<T> wrapper — 401 throws HttpException
                apiService.getUserProfile("any-uuid")
            } catch (e: retrofit2.HttpException) {
                statusCode = e.code()
            }

            // THEN: app receives 401 and must redirect to login (UC 1.7)
            assertEquals(401, statusCode)
        }

    // ─────────────────────────────────────────────
    // UC 1.2 — Expired token — server returns 401
    // Same HTTP code as no-auth — the app treats both identically
    // ─────────────────────────────────────────────

    @Test
    fun `UC 1-2 expired token returns 401 same as no token`() =
        runTest {
            // GIVEN: server identifies the token as expired
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(401)
                    .setBody("""{"error": "TOKEN_EXPIRED"}""")
                    .addHeader("Content-Type", "application/json"),
            )

            var statusCode = 0
            try {
                apiService.getUserProfile("any-uuid")
            } catch (e: retrofit2.HttpException) {
                statusCode = e.code()
                // errorBody carries the reason — use it to differentiate expired vs missing
                val errorBody = e.response()?.errorBody()?.string()
                assertNotNull(errorBody)
                assertTrue(errorBody!!.contains("TOKEN_EXPIRED"))
            }

            assertEquals(401, statusCode)
        }
}
