package com.example.neupsipromovil.presentation.screens.forum.publiDetail

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neupsipromovil.data.repository.ForumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CreatePostUiState {
    object Idle    : CreatePostUiState
    object Loading : CreatePostUiState
    object Success : CreatePostUiState
    data class Error(val message: String) : CreatePostUiState
}

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val forumRepository: ForumRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<CreatePostUiState>(CreatePostUiState.Idle)
    val uiState: StateFlow<CreatePostUiState> = _uiState.asStateFlow()

    private val _titulo    = MutableStateFlow("")
    val titulo: StateFlow<String> = _titulo.asStateFlow()

    private val _contenido = MutableStateFlow("")
    val contenido: StateFlow<String> = _contenido.asStateFlow()

    private val _imagenUri = MutableStateFlow<Uri?>(null)
    val imagenUri: StateFlow<Uri?> = _imagenUri.asStateFlow()

    // 5 MB limit
    private val MAX_IMAGE_BYTES = 5 * 1024 * 1024L

    fun onTituloChanged(value: String)    { _titulo.value    = value }
    fun onContenidoChanged(value: String) { _contenido.value = value }

    fun onImageSelected(uri: Uri?, sizeBytes: Long?) {
        if (uri == null) return
        if (sizeBytes != null && sizeBytes > MAX_IMAGE_BYTES) {
            _uiState.value = CreatePostUiState.Error("La imagen no puede superar 5 MB")
            return
        }
        _imagenUri.value = uri
        if (_uiState.value is CreatePostUiState.Error) _uiState.value = CreatePostUiState.Idle
    }

    fun removeImage() { _imagenUri.value = null }

    fun resetState() { _uiState.value = CreatePostUiState.Idle }

    val canSubmit: Boolean
        get() = _titulo.value.isNotBlank() && _contenido.value.isNotBlank()

    fun submit() {
        if (!canSubmit) {
            _uiState.value = CreatePostUiState.Error("El título y el contenido son obligatorios")
            return
        }
        viewModelScope.launch {
            _uiState.value = CreatePostUiState.Loading
            forumRepository.createPost(
                titulo    = _titulo.value.trim(),
                contenido = _contenido.value.trim(),
                imagenUri = _imagenUri.value,
            ).onSuccess {
                _uiState.value = CreatePostUiState.Success
            }.onFailure { e ->
                _uiState.value = CreatePostUiState.Error(
                    when {
                        e.message?.contains("SESSION_EXPIRED") == true -> "Sesión expirada, inicia sesión nuevamente"
                        else -> e.message ?: "Error inesperado"
                    }
                )
            }
        }
    }
}
 