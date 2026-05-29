package com.example.neupsipromovil.data.remote.api

import com.example.neupsipromovil.data.remote.dto.UserProfileResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface APIService {
    @POST("auth/logout")
    suspend fun logout(
        @Header("Authorization") authHeader: String,
        @Header("No-Authentication") noAuth: String = "true",
        @Header("Content-Type") contentType: String = "application/json"
    ) : Response<Unit>
    @GET("api/profile/{userId}")
    suspend fun getUserProfile(
        @Path("userId") userId: String
    ): UserProfileResponse
}