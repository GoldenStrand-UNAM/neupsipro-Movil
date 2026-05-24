package com.example.neupsipromovil.data.local

import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthManager @Inject constructor(
    private val prefs: SharedPreferences
) {
    private val tag = "AuthManager"

    companion object {
        private const val KEY_TOKEN = "jwt_token"
    }


    private val _sessionState = MutableStateFlow(false)
    val sessionState: StateFlow<Boolean> = _sessionState.asStateFlow()

    init {
        // Check if the token is valid on app
        _sessionState.value = isLoggedIn()
    }

    // Change sessionState to true and save token
    fun saveToken(token: String) {
        Log.d(tag, "Saving token")
        prefs.edit().putString(KEY_TOKEN, token).apply()
        _sessionState.value = true
    }

    // Function to get the token
    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    /// decode the token and check if it is valid
    fun isLoggedIn(): Boolean {
        val payload = decodePayload() ?: run {
            if (getToken() != null) clearSession()
            return false
        }
        val expSeconds = payload.optLong("exp", 0L)
        val isValid = expSeconds * 1000L > System.currentTimeMillis()
        // Auto-clean expired token
        if (!isValid) clearSession()

        return isValid
    }

    fun clearSession() {
        Log.d(tag, "Clearing session")
        prefs.edit().remove(KEY_TOKEN).apply()
        _sessionState.value = false
    }

    // Helper function to decode the token
    /*
    * The token is split in 3 Parts, delimited by a .
    * we focus in Part[1] = userId, userRole, exp, iat, session
     */
    private fun decodePayload(): JSONObject? {
        val token = getToken() ?: return null
        return try {
            val parts = token.split(".")
            if (parts.size < 2) return null
            val payloadJson = String(
                Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_PADDING)
            )
            JSONObject(payloadJson)
        } catch (e: Exception) {
            Log.w(tag, "Couldn´t decode JWT: ${e.message}")
            null
        }
    }
    fun getUserId(): String? {
        val payload = decodePayload() ?: return null
        return try {
            payload.getString("userId")
        } catch (e: Exception) {
            Log.w(tag, "Couldn't get userId from JWT: ${e.message}")
            null
        }
    }
    fun getSessionId(): String? {
        val payload = decodePayload() ?: return null
        println("DEBUG_JWT: $payload")
        return try {
            payload.getString("session")
        } catch (e: Exception) {
            Log.w(tag, "Couldn't get sessionId from JWT: ${e.message}")
            null
        }
    }
}