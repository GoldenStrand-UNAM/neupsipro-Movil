package com.app.neupsiproMovil

import com.example.neupsipromovil.data.local.AuthManager
import com.example.neupsipromovil.data.remote.api.ForumApiService
import com.example.neupsipromovil.data.remote.dto.ForumDataDto
import com.example.neupsipromovil.data.remote.dto.ForumResponse
import com.example.neupsipromovil.data.remote.dto.PaginationDto
import com.example.neupsipromovil.data.remote.dto.PostDto
import com.example.neupsipromovil.data.repository.ForumRepository
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response
import java.io.IOException

class ForumRepositoryTest {

    private lateinit var api: ForumApiService
    private lateinit var authManager: AuthManager
    private lateinit var repository: ForumRepository

    // ── DTOs de prueba ────────────────────────────────────────────────────────
    private val fakePosts = listOf(
        PostDto(
            id = "1",
            title = "Post de prueba",
            content = "Contenido de prueba",
            image = null,
            date = "2024-01-01",
            author = "TestUser",
            pp = null,
        ),
        PostDto(
            id = "2",
            title = "Otro post",
            content = "Otro contenido",
            image = "https://example.com/img.jpg",
            date = "2024-01-02",
            author = "OtroUser",
            pp = "https://example.com/pp.jpg",
        ),
    )

    private val fakeForumResponse = ForumResponse(
        data = ForumDataDto(
            posts = fakePosts,
            pagination = PaginationDto(
                page = 1,
                limit = 10,
                total = 2,
                totalPages = 1,
            ),
        ),
    )

    @Before
    fun setUp() {
        api         = mock()   // Mockito — no tiene el bug de MockK con arm64
        authManager = mock()
        repository  = ForumRepository(api, authManager)
    }

    // ── Tests ─────────────────────────────────────────────────────────────────

    @Test
    fun getPosts_respuestaExitosa_retornaForumPageConPosts() = runTest {
        // GIVEN
        whenever(api.getPosts(1, 10)).thenReturn(Response.success(fakeForumResponse))

        // WHEN
        val result = repository.getPosts(page = 1, limit = 10)

        // THEN
        Assert.assertTrue("Se esperaba éxito", result.isSuccess)
        val forumPage = result.getOrThrow()
        Assert.assertEquals(2, forumPage.posts.size)
        Assert.assertEquals("Post de prueba", forumPage.posts.first().title)
        Assert.assertEquals("TestUser", forumPage.posts.first().author)
    }

    @Test
    fun getPosts_respuesta401_limpiaSesionYRetornaError() = runTest {
        // GIVEN — el servidor responde 401 (token expirado)
        whenever(api.getPosts(1, 10)).thenReturn(
            Response.error(401, "Unauthorized".toResponseBody())
        )

        // WHEN
        val result = repository.getPosts()

        // THEN
        Assert.assertTrue("Se esperaba fallo", result.isFailure)
        Assert.assertEquals("SESSION_EXPIRED", result.exceptionOrNull()?.message)
        verify(authManager).clearSession()
    }

    @Test
    fun getPosts_cuerpoNulo_retornaError() = runTest {
        // GIVEN — HTTP 200 pero body es null
        whenever(api.getPosts(1, 10)).thenReturn(Response.success(null))

        // WHEN
        val result = repository.getPosts()

        // THEN
        Assert.assertTrue("Se esperaba fallo por body vacío", result.isFailure)
        Assert.assertTrue(
            result.exceptionOrNull()?.message?.contains("Empty body") == true
        )
    }

    @Test
    fun getPosts_excepcionDeRed_retornaFailure() = runTest {
        // GIVEN — simula un error de red (sin conexión)

        whenever(api.getPosts(1, 10)).thenAnswer {
            throw IOException("Sin conexión a internet")
        }

        // WHEN
        val result = repository.getPosts()

        // THEN
        Assert.assertTrue("Se esperaba fallo", result.isFailure)
        Assert.assertEquals("Sin conexión a internet", result.exceptionOrNull()?.message)
    }

    @Test
    fun getPosts_listaVacia_retornaForumPageSinPosts() = runTest {
        // GIVEN — el servidor devuelve una lista vacía
        val emptyResponse = ForumResponse(
            data = ForumDataDto(
                posts = emptyList(),
                pagination = PaginationDto(
                    page = 1,
                    limit = 10,
                    total = 0,
                    totalPages = 0,
                ),
            ),
        )
        whenever(api.getPosts(1, 10)).thenReturn(Response.success(emptyResponse))

        // WHEN
        val result = repository.getPosts()

        // THEN
        Assert.assertTrue(result.isSuccess)
        Assert.assertTrue(result.getOrThrow().posts.isEmpty())
    }
}