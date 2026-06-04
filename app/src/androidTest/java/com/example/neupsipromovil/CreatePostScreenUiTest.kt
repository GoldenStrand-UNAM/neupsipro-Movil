package com.example.neupsipromovil

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.neupsipromovil.presentation.screens.forum.PostForum.CreatePostBanner
import com.example.neupsipromovil.presentation.screens.forum.PostForum.CreatePostImagePicker
import com.example.neupsipromovil.presentation.screens.forum.PostForum.CreatePostSubmitButton
import com.example.neupsipromovil.presentation.screens.forum.publiDetail.CreatePostUiState
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CreatePostScreenUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ── Banner ────────────────────────────────────────────────────────────────

    @Test
    fun banner_muestraTextoDelForo() {
        composeTestRule.setContent {
            CreatePostBanner()
        }

        composeTestRule.onNodeWithText("FORO NEUPSI- PRO").assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Comparte conocimiento con la comunidad")
            .assertIsDisplayed()
    }

    // ── Botón de publicar ─────────────────────────────────────────────────────

    @Test
    fun submitButton_isEnabled_whenNotLoading() {
        composeTestRule.setContent {
            CreatePostSubmitButton(isLoading = false, onClick = {})
        }

        composeTestRule.onNodeWithText("Publicar").assertIsDisplayed()
        composeTestRule.onNodeWithText("Publicar").assertIsEnabled()
    }

    @Test
    fun submitButton_isDisabled_whenLoading() {
        composeTestRule.setContent {
            CreatePostSubmitButton(isLoading = true, onClick = {})
        }

        // Cuando carga, el texto "Publicar" no aparece — hay un CircularProgressIndicator
        composeTestRule.onNodeWithText("Publicar").assertDoesNotExist()
    }

    @Test
    fun submitButton_onClick_isTriggered() {
        var clicked = false

        composeTestRule.setContent {
            CreatePostSubmitButton(isLoading = false, onClick = { clicked = true })
        }

        composeTestRule.onNodeWithText("Publicar").performClick()

        assertTrue("Se esperaba que onClick fuera llamado", clicked)
    }

    @Test
    fun submitButton_cuandoCarga_noDispararaClick() {
        var clicked = false

        composeTestRule.setContent {
            CreatePostSubmitButton(isLoading = true, onClick = { clicked = true })
        }

        // El botón está deshabilitado — no debe disparar el callback
        composeTestRule.onNodeWithContentDescription("Publicar").assertDoesNotExist()
        assertFalse("No debería haberse llamado onClick", clicked)
    }

    // ── Image Picker ──────────────────────────────────────────────────────────

    @Test
    fun imagePicker_sinImagen_muestraPlaceholder() {
        composeTestRule.setContent {
            com.example.neupsipromovil.presentation.screens.forum.PostForum.CreatePostImagePicker(
                imagenUri = null,
                enabled   = true,
                onPick    = {},
                onCamera  = {},
                onRemove  = {},
            )
        }

        composeTestRule.onNodeWithText("Añade una imagen").assertIsDisplayed()
        composeTestRule.onNodeWithText("Máximo 5 MB").assertIsDisplayed()
        composeTestRule.onNodeWithText("Galería").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cámara").assertIsDisplayed()
    }

    @Test
    fun imagePicker_botonGaleria_dispararaCallback() {
        var galeriaCalled = false

        composeTestRule.setContent {
            com.example.neupsipromovil.presentation.screens.forum.PostForum.CreatePostImagePicker(
                imagenUri = null,
                enabled   = true,
                onPick    = { galeriaCalled = true },
                onCamera  = {},
                onRemove  = {},
            )
        }

        composeTestRule.onNodeWithText("Galería").performClick()

        assertTrue("Se esperaba que onPick fuera llamado", galeriaCalled)
    }

    @Test
    fun imagePicker_botonCamara_dispararaCallback() {
        var camaraCalled = false

        composeTestRule.setContent {
            com.example.neupsipromovil.presentation.screens.forum.PostForum.CreatePostImagePicker (
                imagenUri = null,
                enabled   = true,
                onPick    = {},
                onCamera  = { camaraCalled = true },
                onRemove  = {},
            )
        }

        composeTestRule.onNodeWithText("Cámara").performClick()

        assertTrue("Se esperaba que onCamera fuera llamado", camaraCalled)
    }

    @Test
    fun imagePicker_botonesDeshabilitados_cuandoNotEnabled() {
        composeTestRule.setContent {
            com.example.neupsipromovil.presentation.screens.forum.PostForum.CreatePostImagePicker(
                imagenUri = null,
                enabled   = false,
                onPick    = {},
                onCamera  = {},
                onRemove  = {},
            )
        }

        composeTestRule.onNodeWithText("Galería").assertIsNotEnabled()
        composeTestRule.onNodeWithText("Cámara").assertIsNotEnabled()
    }

    @Test
    fun imagePicker_conImagen_muestraBotónQuitarImagen() {
        val fakeUri = android.net.Uri.parse("content://fake/image.jpg")

        composeTestRule.setContent {
            com.example.neupsipromovil.presentation.screens.forum.PostForum.CreatePostImagePicker(
                imagenUri = fakeUri,
                enabled   = true,
                onPick    = {},
                onCamera  = {},
                onRemove  = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription("Quitar imagen")
            .assertIsDisplayed()
    }

    @Test
    fun imagePicker_botonQuitar_dispararaCallback() {
        val fakeUri   = android.net.Uri.parse("content://fake/image.jpg")
        var removeCalled = false

        composeTestRule.setContent {
            com.example.neupsipromovil.presentation.screens.forum.PostForum.CreatePostImagePicker(
                imagenUri = fakeUri,
                enabled   = true,
                onPick    = {},
                onCamera  = {},
                onRemove  = { removeCalled = true },
            )
        }

        composeTestRule
            .onNodeWithContentDescription("Quitar imagen")
            .performClick()

        assertTrue("Se esperaba que onRemove fuera llamado", removeCalled)
    }

    @Test
    fun imagePicker_conImagen_noMuestraPlaceholder() {
        val fakeUri = android.net.Uri.parse("content://fake/image.jpg")

        composeTestRule.setContent {
            com.example.neupsipromovil.presentation.screens.forum.PostForum.CreatePostImagePicker(
                imagenUri = fakeUri,
                enabled   = true,
                onPick    = {},
                onCamera  = {},
                onRemove  = {},
            )
        }

        composeTestRule.onNodeWithText("Añade una imagen").assertDoesNotExist()
        composeTestRule.onNodeWithText("Galería").assertDoesNotExist()
        composeTestRule.onNodeWithText("Cámara").assertDoesNotExist()
    }
}
