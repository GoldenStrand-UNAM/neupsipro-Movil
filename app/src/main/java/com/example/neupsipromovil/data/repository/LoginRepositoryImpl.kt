package com.example.neupsipromovil.data.repository

import android.util.Log
import com.example.neupsipromovil.data.local.AuthManager
import com.example.neupsipromovil.data.remote.api.APIService
import com.example.neupsipromovil.data.remote.api.LoginApiService
import com.example.neupsipromovil.domain.model.Login
import com.example.neupsipromovil.domain.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


//Talks to /auth/login and persists the resulting JWT through AuthManager.
@Singleton
class LoginRepositoryImpl @Inject constructor(
    private val loginApiService: LoginApiService,
    private val apiService: APIService,
    private val authManager: AuthManager
) : LoginRepository {

    private val tag = "LoginRepository"

    override suspend fun login(username: String, password: String): Result<Login> =
        // Switch to IO thread
        withContext(Dispatchers.IO) {
            try {
                // Call endpoint
                val response = loginApiService.postLogin(username, password)
                Log.d(tag, "Response code: ${response.code()}")

                when {
                    response.isSuccessful -> {
                        val token = response.body()?.token
                        if (token.isNullOrBlank()) {
                            Result.failure(Exception("UNKNOWN_ERROR"))
                        } else {
                            // token also flips sessionState to true
                            authManager.saveToken(token)
                            Result.success(Login(username))
                        }
                    }
                    // mapping of backend error codes to domain errors
                    response.code() == 400 -> Result.failure(Exception("INVALID_LENGTH"))
                    response.code() == 401 -> Result.failure(Exception("INVALID_CREDENTIALS"))
                    response.code() == 403 -> Result.failure(Exception("USER_DISABLED"))
                    response.code() == 429 -> Result.failure(Exception("TOO_MANY_REQUESTS"))
                    else -> Result.failure(Exception("UNKNOWN_ERROR"))
                }
            } catch (e: Exception) {
                // Network issues
                Log.e(tag, "Network error: ${e.message}", e)
                Result.failure(Exception("NETWORK_ERROR"))
            }
        }
    override suspend fun logout(): Result<Unit> {
        return try {
            val sessionId = authManager.getSessionId()
            Log.d("DEBUG_LOGOUT", "sessionId: $sessionId")

            if (!sessionId.isNullOrBlank()) {
                val response = apiService.logout(authHeader = "Bearer $sessionId")

                if (response.isSuccessful) {
                    Log.d("DEBUG_LOGOUT", "Logout exitoso")
                    authManager.clearSession()
                    Result.success(Unit)
                } else {
                    Log.e("DEBUG_LOGOUT", "Error en el servidor: ${response.code()}")
                    authManager.clearSession()
                    Result.failure(Exception("Error en el servidor: ${response.code()}"))
                }
            } else {
                authManager.clearSession()
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Log.e("DEBUG_LOGOUT", "Error al cerrar sesión: ${e.message}")
            authManager.clearSession()
            Result.failure(Exception("Error al cerrar sesión: ${e.message}"))
        }
    }
}