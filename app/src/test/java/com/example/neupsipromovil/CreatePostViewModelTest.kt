package com.example.neupsipromovil

import android.net.Uri
import com.example.neupsipromovil.data.repository.ForumRepository
import com.example.neupsipromovil.presentation.screens.forum.publiDetail.CreatePostUiState
import com.example.neupsipromovil.presentation.screens.forum.publiDetail.CreatePostViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreatePostViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var forumRepository: ForumRepository
    private lateinit var viewModel: CreatePostViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        forumRepository = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ── Estado inicial ────────────────────────────────────────────────────────

    @Test
    fun initialState_isIdle() {
        viewModel = CreatePostViewModel(forumRepository)

        Assert.assertTrue(
            "Se esperaba estado Idle al inicio",
            viewModel.uiState.value is CreatePostUiState.Idle,
        )
    }

    @Test
    fun initialState_fieldsAreEmpty() {
        viewModel = CreatePostViewModel(forumRepository)

        Assert.assertEquals("", viewModel.titulo.value)
        Assert.assertEquals("", viewModel.contenido.value)
        Assert.assertNull(viewModel.imagenUri.value)
    }

    // ── Cambios de campos ─────────────────────────────────────────────────────

    @Test
    fun onTituloChanged_updatesFlow() {
        viewModel = CreatePostViewModel(forumRepository)

        viewModel.onTituloChanged("Mi nuevo post")

        Assert.assertEquals("Mi nuevo post", viewModel.titulo.value)
    }

    @Test
    fun onContenidoChanged_updatesFlow() {
        viewModel = CreatePostViewModel(forumRepository)

        viewModel.onContenidoChanged("Este es el contenido del post")

        Assert.assertEquals("Este es el contenido del post", viewModel.contenido.value)
    }

    @Test
    fun onImageSelected_updatesUriAndRejectsIfTooLarge() {
        viewModel = CreatePostViewModel(forumRepository)
        val fakeUri = mockk<Uri>()

        // Imagen dentro del límite (1 MB)
        viewModel.onImageSelected(fakeUri, sizeBytes = 1_000_000L)

        Assert.assertEquals(fakeUri, viewModel.imagenUri.value)
        Assert.assertTrue(viewModel.uiState.value is CreatePostUiState.Idle)
    }

    @Test
    fun onImageSelected_rejectsImageOver5MB() {
        viewModel = CreatePostViewModel(forumRepository)
        val fakeUri = mockk<Uri>()

        // Imagen de 6 MB — debe rechazarse
        viewModel.onImageSelected(fakeUri, sizeBytes = 6_000_000L)

        Assert.assertNull(
            "La URI no debe guardarse si la imagen supera 5 MB",
            viewModel.imagenUri.value,
        )
        Assert.assertTrue(
            "Se esperaba estado Error por imagen muy grande",
            viewModel.uiState.value is CreatePostUiState.Error,
        )
    }

    @Test
    fun removeImage_clearsUri() {
        viewModel = CreatePostViewModel(forumRepository)
        val fakeUri = mockk<Uri>()

        viewModel.onImageSelected(fakeUri, sizeBytes = 500_000L)
        viewModel.removeImage()

        Assert.assertNull(viewModel.imagenUri.value)
    }

    // ── Validaciones antes de publicar ────────────────────────────────────────

    @Test
    fun submit_withEmptyTitulo_setsErrorState() = runTest {
        viewModel = CreatePostViewModel(forumRepository)

        viewModel.onTituloChanged("")
        viewModel.onContenidoChanged("Contenido válido")
        viewModel.submit()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        Assert.assertTrue("Se esperaba Error por título vacío", state is CreatePostUiState.Error)
        Assert.assertTrue(
            (state as CreatePostUiState.Error).message.contains("título", ignoreCase = true),
        )
    }

    @Test
    fun submit_withEmptyContenido_setsErrorState() = runTest {
        viewModel = CreatePostViewModel(forumRepository)

        viewModel.onTituloChanged("Título válido")
        viewModel.onContenidoChanged("")
        viewModel.submit()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        Assert.assertTrue("Se esperaba Error por contenido vacío", state is CreatePostUiState.Error)
        Assert.assertTrue(
            (state as CreatePostUiState.Error).message.contains("contenido", ignoreCase = true),
        )
    }

    // ── Flujo exitoso ─────────────────────────────────────────────────────────

    @Test
    fun submit_conDatosValidos_setLoadingLuegoSuccess() = runTest {
        coEvery {
            forumRepository.createPost(any(), any(), any())
        } returns Result.success(Unit)

        viewModel = CreatePostViewModel(forumRepository)

        viewModel.onTituloChanged("Título de prueba")
        viewModel.onContenidoChanged("Contenido de prueba")

        runCurrent()

        viewModel.submit()
        advanceUntilIdle()

        Assert.assertTrue(
            "Se esperaba Success tras publicar",
            viewModel.uiState.value is CreatePostUiState.Success,
        )
    }

    @Test
    fun submit_conImagen_pasaUriAlUseCase() = runTest {
        val fakeUri = mockk<Uri>()

        coEvery {
            forumRepository.createPost(any(), any(), any())
        } returns Result.success(Unit)

        viewModel = CreatePostViewModel(forumRepository)

        viewModel.onTituloChanged("Post con foto")
        viewModel.onContenidoChanged("Contenido con imagen")
        viewModel.onImageSelected(fakeUri, 1_000_000L)

        runCurrent()

        viewModel.submit()
        advanceUntilIdle()

        coVerify(exactly = 1) {
            forumRepository.createPost(
                "Post con foto",
                "Contenido con imagen",
                fakeUri
            )
        }

        Assert.assertTrue(
            viewModel.uiState.value is CreatePostUiState.Success
        )
    }

    // ── Flujo de error ────────────────────────────────────────────────────────

    @Test
    fun submit_cuandoUseCaseFalla_setsErrorState() = runTest {

        coEvery {
            forumRepository.createPost(any(), any(), any())
        } returns Result.failure(
            Exception("Error al publicar")
        )

        @Test
        fun submit_sessionExpired_setsErrorConMensajeEspecifico() = runTest {
            coEvery {
                forumRepository.createPost(any(), any(), any())
            } returns Result.failure(
                Exception("SESSION_EXPIRED")
            )

            viewModel = CreatePostViewModel(forumRepository)
            viewModel.onTituloChanged("Título")
            viewModel.onContenidoChanged("Contenido")

            viewModel.submit()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            Assert.assertTrue(state is CreatePostUiState.Error)
            Assert.assertEquals(
                "Sesión expirada, inicia sesión nuevamente",
                (state as CreatePostUiState.Error).message
            )

        }

        // ── resetState ────────────────────────────────────────────────────────────


    }
}