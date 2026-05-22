package com.example.neupsipromovil.data.remote.api

import com.example.neupsipromovil.data.remote.dto.UserProfileResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {
    @GET("api/profile/{userId}")
    suspend fun getUserProfile(
        @Path("userId") userId: String
    ): UserProfileResponse
}