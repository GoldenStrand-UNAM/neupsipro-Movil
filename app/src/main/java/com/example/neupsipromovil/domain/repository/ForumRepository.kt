package com.example.neupsipromovil.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.neupsipromovil.data.local.AuthManager
import com.example.neupsipromovil.data.remote.api.ForumApiService
import com.example.neupsipromovil.domain.mapper.toDomain
import com.example.neupsipromovil.domain.model.ForumPage
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class ForumRepository @Inject constructor(
    private val api: ForumApiService,
    private val authManager: AuthManager,
    @ApplicationContext private val context: Context,
) {
    suspend fun getPosts(page: Int = 1, limit: Int = 10): Result<ForumPage> =
        runCatching {
            val response = api.getPosts(page = page, limit = limit)

            Log.d("ForumRepository", "HTTP code: ${response.code()}")
            Log.d("ForumRepository", "isSuccessful: ${response.isSuccessful}")

            if (response.code() == 401) {
                authManager.clearSession()
                error("SESSION_EXPIRED")
            }

            val body = response.body()
                ?: error("Empty body (HTTP ${response.code()})")
            body.toDomain()
        }

    suspend fun createPost(
        titulo: String,
        contenido: String,
        imagenUri: Uri?,
    ): Result<Unit> = runCatching {
        val tituloBody    = titulo.toRequestBody("text/plain".toMediaTypeOrNull())
        val contenidoBody = contenido.toRequestBody("text/plain".toMediaTypeOrNull())

        val imagenPart = imagenUri?.let { uri ->
            val mimeType  = context.contentResolver.getType(uri) ?: "image/jpeg"
            val bytes     = context.contentResolver.openInputStream(uri)?.readBytes()
                ?: error("No se pudo leer la imagen")
            val reqBody   = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
            MultipartBody.Part.createFormData("imagen", "imagen.jpg", reqBody)
        }

        val response = api.createPost(tituloBody, contenidoBody, imagenPart)

        if (response.code() == 401) {
            authManager.clearSession()
            error("SESSION_EXPIRED")
        }

        if (!response.isSuccessful) {
            error("Error al publicar (HTTP ${response.code()})")
        }
    }
}