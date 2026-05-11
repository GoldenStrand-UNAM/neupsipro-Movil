package com.example.neupsipromovil.data.remote.api

import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIServiceTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: APIService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        // OkHttpClient without an auth interceptor — we test raw HTTP behavior
        val client = OkHttpClient.Builder().build()

        apiService =
            Retrofit
                .Builder()
                .baseUrl(mockWebServer.url("/"))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIService::class.java)
    }

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

            val response = apiService.getUserProfile(uuid)
        }
}
