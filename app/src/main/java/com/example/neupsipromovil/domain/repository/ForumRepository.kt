package com.example.neupsipromovil.data.repository

import android.util.Log
import com.example.neupsipromovil.data.local.AuthManager
import com.example.neupsipromovil.data.remote.api.ForumApiService
import com.example.neupsipromovil.domain.mapper.toDomain
import com.example.neupsipromovil.domain.model.ForumPage
import javax.inject.Inject

class ForumRepository @Inject constructor(
    private val api: ForumApiService,
    private val authManager: AuthManager,
) {
    suspend fun getPosts(page: Int = 1, limit: Int = 10): Result<ForumPage> =
        runCatching {
            val response = api.getPosts(page = page, limit = limit)

            Log.d("ForumRepository", "HTTP code: ${response.code()}")
            Log.d("ForumRepository", "isSuccessful: ${response.isSuccessful}")

            // Token expirado → limpia sesión, la UI reacciona al Flow
            if (response.code() == 401) {
                authManager.clearSession()
                error("SESSION_EXPIRED")
            }

            val body = response.body()
                ?: error("Empty body (HTTP ${response.code()})")
            body.toDomain()
        }
}