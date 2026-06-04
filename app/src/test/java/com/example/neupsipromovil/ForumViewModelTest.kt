package com.example.neupsipromovil

import com.example.neupsipromovil.domain.model.ForumPage
import com.example.neupsipromovil.domain.model.ForumPost
import com.example.neupsipromovil.domain.usecase.forum.GetForumPostsUseCase
import com.example.neupsipromovil.presentation.screens.forum.ForumUiState
import com.example.neupsipromovil.presentation.screens.forum.ForumViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import io.mockk.mockk


@OptIn(ExperimentalCoroutinesApi::class)
class ForumViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getForumPostsUseCase: GetForumPostsUseCase
    private lateinit var viewModel: ForumViewModel

    private val fakePosts = listOf(
        ForumPost(id = "1", title = "Kotlin es genial", content = "Aprende Kotlin desde cero",
            imageUrl = null, date = "2024-01-01", author = "Ana", avatarUrl = null),
        ForumPost(id = "2", title = "Android Studio tips", content = "Mejora tu productividad",
            imageUrl = null, date = "2024-01-02", author = "Luis", avatarUrl = null),
        ForumPost(id = "3", title = "Compose vs XML", content = "Diferencias entre ambos enfoques",
            imageUrl = null, date = "2024-01-03", author = "Ana", avatarUrl = null),
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getForumPostsUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getAll() = runTest {
        coEvery { getForumPostsUseCase(any(), any()) } returns
                Result.success(ForumPage(posts = fakePosts, page = 1, totalPages = 1))

        viewModel = ForumViewModel(getForumPostsUseCase)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        Assert.assertTrue("Se esperaba Success", state is ForumUiState.Success)
        Assert.assertEquals(3, (state as ForumUiState.Success).posts.size)
    }

    @Test
    fun getFailed() = runTest {
        coEvery { getForumPostsUseCase(any(), any()) } returns
                Result.failure(Exception("Sin conexión"))

        viewModel = ForumViewModel(getForumPostsUseCase)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        Assert.assertTrue("Se esperaba Error", state is ForumUiState.Error)
        Assert.assertEquals("Sin conexión", (state as ForumUiState.Error).message)
    }

    @Test
    fun searchByTitle() = runTest {
        coEvery { getForumPostsUseCase(any(), any()) } returns
                Result.success(ForumPage(posts = fakePosts, page = 1, totalPages = 1))

        viewModel = ForumViewModel(getForumPostsUseCase)
        advanceUntilIdle()

        viewModel.onSearchQueryChange("Kotlin")

        val state = viewModel.uiState.value as ForumUiState.Success
        Assert.assertEquals(1, state.posts.size)
        Assert.assertEquals("Kotlin es genial", state.posts.first().title)
    }

    @Test
    fun searchByAutor() = runTest {
        coEvery { getForumPostsUseCase(any(), any()) } returns
                Result.success(ForumPage(posts = fakePosts, page = 1, totalPages = 1))

        viewModel = ForumViewModel(getForumPostsUseCase)
        advanceUntilIdle()

        viewModel.onSearchQueryChange("Ana")

        val state = viewModel.uiState.value as ForumUiState.Success
        Assert.assertEquals(2, state.posts.size)
        Assert.assertTrue(state.posts.all { it.author == "Ana" })
    }

    @Test
    fun cleanAll() = runTest {
        coEvery { getForumPostsUseCase(any(), any()) } returns
                Result.success(ForumPage(posts = fakePosts, page = 1, totalPages = 1))

        viewModel = ForumViewModel(getForumPostsUseCase)
        advanceUntilIdle()

        viewModel.onSearchQueryChange("Kotlin")
        viewModel.onSearchQueryChange("")

        val state = viewModel.uiState.value as ForumUiState.Success
        Assert.assertEquals(3, state.posts.size)
    }

    @Test
    fun retryAll() = runTest {
        coEvery { getForumPostsUseCase(any(), any()) } returnsMany listOf(
            Result.failure(Exception("Error de red")),
            Result.success(ForumPage(posts = fakePosts, page = 1, totalPages = 1)),
        )

        viewModel = ForumViewModel(getForumPostsUseCase)
        advanceUntilIdle()

        viewModel.retry()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        Assert.assertTrue(state is ForumUiState.Success)
        coVerify(exactly = 2) { getForumPostsUseCase(any(), any()) }
    }

    @Test
    fun loadginAll() {
        coEvery { getForumPostsUseCase(any(), any()) } returns
                Result.success(ForumPage(posts = fakePosts, page = 1, totalPages = 1))

        val freshViewModel = ForumViewModel(getForumPostsUseCase)

        Assert.assertTrue(freshViewModel.uiState.value is ForumUiState.Loading)
    }
}