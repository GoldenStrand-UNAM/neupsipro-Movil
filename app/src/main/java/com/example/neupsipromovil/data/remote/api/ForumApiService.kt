package com.example.neupsipromovil.data.remote.api

import com.example.neupsipromovil.data.remote.dto.ForumResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ForumApiService {

    @GET("api/forum")
    suspend fun getPosts(
        @Query("page")  page:  Int = 1,
        @Query("limit") limit: Int = 10,
    ): Response<ForumResponse>
}
