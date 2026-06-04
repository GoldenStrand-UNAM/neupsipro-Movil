package com.example.neupsipromovil.data.remote.api

import com.example.neupsipromovil.data.remote.dto.ForumResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ForumApiService {

    @GET("api/forum")
    suspend fun getPosts(
        @Query("page")  page:  Int = 1,
        @Query("limit") limit: Int = 10,
    ): Response<ForumResponse>

    @Multipart
    @POST("api/publication")
    suspend fun createPost(
        @Part("titulo")    titulo:    RequestBody,
        @Part("contenido") contenido: RequestBody,
        @Part imagen:      MultipartBody.Part?,
    ): Response<Unit>
}