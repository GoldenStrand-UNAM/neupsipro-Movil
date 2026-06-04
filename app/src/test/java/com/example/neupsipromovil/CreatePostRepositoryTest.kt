package com.example.neupsipromovil

import android.content.Context
import android.net.Uri
import com.example.neupsipromovil.data.local.AuthManager
import com.example.neupsipromovil.data.remote.api.ForumApiService
import com.example.neupsipromovil.data.repository.ForumRepository
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class CreatePostRepositoryTest {

    private lateinit var api: ForumApiService
    private lateinit var authManager: AuthManager
    private lateinit var context: Context
    private lateinit var repository: ForumRepository

    @Before
    fun setUp() {
        api         = mock()
        authManager = mock()
        context     = mock()
        repository  = ForumRepository(api, authManager, context)
    }


    @Test
    fun createPostWithoutImage() = runTest {
        whenever(api.createPost(any(), any(), anyOrNull()))
            .thenReturn(Response.success(Unit))

        val result = repository.createPost("Título", "Contenido", null)

        Assert.assertTrue("Se esperaba éxito", result.isSuccess)
    }

    @Test
    fun createPostWithOutImage2() = runTest {
        // GIVEN
        whenever(api.createPost(any(), any(), anyOrNull())).thenReturn(Response.success(Unit))
        // WHEN
        repository.createPost(
            titulo    = "Título sin foto",
            contenido = "Solo texto",
            imagenUri = null,
        )

        verify(api).createPost(
            any(),
            any(),
            org.mockito.kotlin.isNull(),
        )
    }


    @Test
    fun createPostWithImage() = runTest {
        // GIVEN
        val fakeUri          = mock<Uri>()
        val fakeInputStream  = "fake_image_bytes".byteInputStream()
        val fakeAssetFd      = mock<android.content.res.AssetFileDescriptor>()

        whenever(context.contentResolver).thenReturn(mock())
        whenever(context.contentResolver.getType(fakeUri)).thenReturn("image/jpeg")
        whenever(context.contentResolver.openInputStream(fakeUri)).thenReturn(fakeInputStream)
        whenever(api.createPost(any(), any(), anyOrNull())).thenReturn(Response.success(Unit))

        // WHEN
        val result = repository.createPost(
            titulo    = "Post con foto",
            contenido = "Contenido con imagen",
            imagenUri = fakeUri,
        )

        // THEN
        Assert.assertTrue("Se esperaba éxito al subir imagen", result.isSuccess)
        verify(api).createPost(any(), any(), any())
    }

    @Test
    fun createPostNull() = runTest {
        // GIVEN
        val fakeUri = mock<Uri>()
        whenever(context.contentResolver).thenReturn(mock())
        whenever(context.contentResolver.getType(fakeUri)).thenReturn("image/jpeg")
        whenever(context.contentResolver.openInputStream(fakeUri)).thenReturn(null)

        // WHEN
        val result = repository.createPost(
            titulo    = "Título",
            contenido = "Contenido",
            imagenUri = fakeUri,
        )

        // THEN
        Assert.assertTrue("Se esperaba fallo si no se puede leer la imagen", result.isFailure)
        Assert.assertTrue(
            result.exceptionOrNull()?.message?.contains("imagen", ignoreCase = true) == true,
        )
    }


    @Test
    fun createPost401() = runTest {
        whenever(api.createPost(any(), any(), anyOrNull()))
            .thenReturn(Response.error(401, "Unauthorized".toResponseBody()))

        val result = repository.createPost("Título", "Contenido", null)

        Assert.assertTrue(result.isFailure)
        Assert.assertEquals("SESSION_EXPIRED", result.exceptionOrNull()?.message)
        verify(authManager).clearSession()
    }

    @Test
    fun createPost500() = runTest {
        whenever(api.createPost(any(), any(), anyOrNull()))
            .thenReturn(Response.error(500, "Internal Server Error".toResponseBody()))

        val result = repository.createPost("Título", "Contenido", null)

        Assert.assertTrue(result.isFailure)
        Assert.assertTrue(result.exceptionOrNull()?.message?.contains("500") == true)
        verify(authManager, never()).clearSession()
    }

    @Test
    fun createPost400() = runTest {
        whenever(api.createPost(any(), any(), anyOrNull()))
            .thenReturn(Response.error(400, "Bad Request".toResponseBody()))

        val result = repository.createPost("Título", "Contenido", null)

        Assert.assertTrue(result.isFailure)
        verify(authManager, never()).clearSession()
    }


    @Test
    fun createPost501() = runTest {
        whenever(api.createPost(any(), any(), anyOrNull()))
            .thenAnswer { throw IOException("Sin conexión a internet") }

        val result = repository.createPost("Título", "Contenido", null)

        Assert.assertTrue(result.isFailure)
        Assert.assertEquals("Sin conexión a internet", result.exceptionOrNull()?.message)
    }


    @Test
    fun createPostTimeOut() = runTest {
        whenever(api.createPost(any(), any(), anyOrNull()))  // 👈
            .thenAnswer { throw SocketTimeoutException("timeout") }

        val result = repository.createPost("Título", "Contenido", null)

        Assert.assertTrue(result.isFailure)
        Assert.assertEquals("timeout", result.exceptionOrNull()?.message)
    }
}
