package com.example.neupsipromovil.data.remote.api

import com.example.neupsipromovil.data.local.AuthManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthInterceptor @Inject constructor(
    private val authManager: AuthManager,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val isLoginCall     = original.url.encodedPath.contains("/auth/login")
        val hasNoAuthHeader = original.header("No-Authentication") != null

        val request = original.newBuilder()
            .header("Accept", "application/json")
            .removeHeader("No-Authentication")
            .apply {
                if (!isLoginCall && !hasNoAuthHeader) {
                    authManager.getToken()?.let { token ->
                        header("Authorization", "Bearer $token")
                    }
                }
            }
            .build()

        android.util.Log.d("AuthInterceptor", "URL: ${request.url}")
        android.util.Log.d("AuthInterceptor", "Authorization: ${request.header("Authorization")}")

        val response = chain.proceed(request)

        if (response.code == 401 && !isLoginCall) {
            authManager.clearSession()
        }

        return response
    }
}