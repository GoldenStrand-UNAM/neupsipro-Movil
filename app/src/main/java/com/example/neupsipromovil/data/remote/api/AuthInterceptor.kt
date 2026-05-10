package com.example.neupsipromovil.data.remote.api

import com.example.neupsipromovil.data.local.AuthManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton
/*
* HTTP client that captures outgoing requests
* to automatically add an authorization token (JWT) to the header.
*/

@Singleton
class AuthInterceptor @Inject constructor(
    private val authManager: AuthManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        // If is a login call doesnt add the bearer
        val isLoginCall = original.url.encodedPath.contains("/auth/login")

        // ask to the response to be a json
        val builder = original.newBuilder()
            .header("Accept", "application/json")

        // if is any other call adds the bearer header
        if (!isLoginCall) {
            authManager.getToken()?.let { token ->
                builder.header("Authorization", "Bearer $token")
            }
        }

        val response = chain.proceed(builder.build())

        // if the token is not longer valid
        if (response.code == 401 && !isLoginCall) {
            authManager.clearSession()
        }
        return response
    }
}