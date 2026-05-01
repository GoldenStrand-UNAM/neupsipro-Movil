package com.example.neupsipromovil.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Handles user authentication in the application
 * Defines the API endpoints for login operations using Retrofit
 */
interface AuthApiService {

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun postLogin(
        @Field("username") userName: String,
        @Field("password") password: String
    ): Response<ResponseBody>
}
