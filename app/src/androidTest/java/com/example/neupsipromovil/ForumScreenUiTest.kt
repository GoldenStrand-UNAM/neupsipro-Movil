package com.example.neupsipromovil

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.neupsipromovil.domain.model.ForumPost
import com.example.neupsipromovil.presentation.screens.forum.ForumScreenContent
import com.example.neupsipromovil.presentation.screens.forum.ForumUiState
import org.junit.Rule
import org.junit.Test

class ForumScreenUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakePosts = listOf(
        ForumPost(id = "1", title = "Kotlin es genial", content = "Aprende Kotlin desde cero",
            imageUrl = null, date = "2024-01-01", author = "Ana", avatarUrl = null),
        ForumPost(id = "2", title = "Android Studio tips", content = "Mejora tu productividad",
            imageUrl = null, date = "2024-01-02", author = "Luis", avatarUrl = null),
        ForumPost(id = "3", title = "Compose vs XML", content = "Diferencias entre ambos enfoques",
            imageUrl = null, date = "2024-01-03", author = "Ana", avatarUrl = null),
    )



    @Test
    fun loading_state_shows_circular_indicator() {
        composeTestRule.setContent {
            ForumScreenContent(
                uiState             = ForumUiState.Loading,
                searchQuery         = "",
                onSearchQueryChange = {},
                onRetry             = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription("Cargando")
            .assertIsDisplayed()
    }


    @Test
    fun success_state_shows_posts_list() {
        composeTestRule.setContent {
            ForumScreenContent(
                uiState             = ForumUiState.Success(fakePosts),
                searchQuery         = "",
                onSearchQueryChange = {},
                onRetry             = {},
            )
        }

        composeTestRule.onNodeWithText("Kotlin es genial").assertIsDisplayed()
        composeTestRule.onNodeWithText("Android Studio tips").assertIsDisplayed()
        composeTestRule.onNodeWithText("Compose vs XML").assertIsDisplayed()
    }

    @Test
    fun success_state_with_empty_list_shows_empty_message() {
        composeTestRule.setContent {
            ForumScreenContent(
                uiState             = ForumUiState.Success(emptyList()),
                searchQuery         = "",
                onSearchQueryChange = {},
                onRetry             = {},
            )
        }

        composeTestRule.onNodeWithText("No hay publicaciones").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sé el primero en publicar algo en el foro.").assertIsDisplayed()
    }


    @Test
    fun error_state_shows_message_and_retry_button() {
        composeTestRule.setContent {
            ForumScreenContent(
                uiState             = ForumUiState.Error("Sin conexión"),
                searchQuery         = "",
                onSearchQueryChange = {},
                onRetry             = {},
            )
        }

        composeTestRule.onNodeWithText("Algo salió mal").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sin conexión").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reintentar").assertIsDisplayed()
    }

    @Test
    fun error_state_retry_button_triggers_callback() {
        var retryCalled = false

        composeTestRule.setContent {
            ForumScreenContent(
                uiState             = ForumUiState.Error("Sin conexión"),
                searchQuery         = "",
                onSearchQueryChange = {},
                onRetry             = { retryCalled = true },
            )
        }

        composeTestRule.onNodeWithText("Reintentar").performClick()

        assert(retryCalled) { "Se esperaba que onRetry fuera llamado al hacer click en Reintentar" }
    }


    @Test
    fun search_field_placeholder_is_visible() {
        composeTestRule.setContent {
            ForumScreenContent(
                uiState             = ForumUiState.Success(fakePosts),
                searchQuery         = "",
                onSearchQueryChange = {},
                onRetry             = {},
            )
        }

        composeTestRule.onNodeWithText("Buscar discusiones ...").assertIsDisplayed()
    }

    @Test
    fun typing_in_search_field_triggers_callback() {
        var capturedQuery = ""

        composeTestRule.setContent {
            ForumScreenContent(
                uiState             = ForumUiState.Success(fakePosts),
                searchQuery         = "",
                onSearchQueryChange = { capturedQuery = it },
                onRetry             = {},
            )
        }

        composeTestRule
            .onNodeWithText("Buscar discusiones ...")
            .performTextInput("Kotlin")

        assert(capturedQuery == "Kotlin") {
            "Se esperaba 'Kotlin' pero se recibió '$capturedQuery'"
        }
    }

    @Test
    fun search_by_title_shows_only_matching_posts() {
        // Simulamos el estado ya filtrado (el filtro ocurre en el ViewModel)
        val filtered = fakePosts.filter { it.title.contains("Kotlin", ignoreCase = true) }

        composeTestRule.setContent {
            ForumScreenContent(
                uiState             = ForumUiState.Success(filtered),
                searchQuery         = "Kotlin",
                onSearchQueryChange = {},
                onRetry             = {},
            )
        }

        composeTestRule.onNodeWithText("Kotlin es genial").assertIsDisplayed()
        composeTestRule.onNodeWithText("Android Studio tips").assertDoesNotExist()
        composeTestRule.onNodeWithText("Compose vs XML").assertDoesNotExist()
    }


    @Test
    fun search_by_author_shows_only_matching_posts() {
        val filtered = fakePosts.filter { it.author.contains("Ana", ignoreCase = true) }

        composeTestRule.setContent {
            ForumScreenContent(
                uiState             = ForumUiState.Success(filtered),
                searchQuery         = "Ana",
                onSearchQueryChange = {},
                onRetry             = {},
            )
        }

        composeTestRule.onNodeWithText("Kotlin es genial").assertIsDisplayed()
        composeTestRule.onNodeWithText("Compose vs XML").assertIsDisplayed()
        composeTestRule.onNodeWithText("Android Studio tips").assertDoesNotExist()
    }



    @Test
    fun search_with_no_results_shows_empty_state() {
        composeTestRule.setContent {
            ForumScreenContent(
                uiState             = ForumUiState.Success(emptyList()),
                searchQuery         = "xyzabc",
                onSearchQueryChange = {},
                onRetry             = {},
            )
        }

        composeTestRule.onNodeWithText("Sin resultados").assertIsDisplayed()
        composeTestRule
            .onNodeWithText("No encontramos publicaciones para \"xyzabc\".")
            .assertIsDisplayed()
    }
}