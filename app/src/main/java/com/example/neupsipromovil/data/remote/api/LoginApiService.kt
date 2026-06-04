package com.example.neupsipromovil.data.remote.api


import com.example.neupsipromovil.data.remote.dto.LoginResponseDto
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

//Defines the API endpoints for login operations using Retrofit
interface LoginApiService {

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun postLogin(
        @Field("username") userName: String,
        @Field("password") password: String
    ): Response<LoginResponseDto>
}
//Get token as response


