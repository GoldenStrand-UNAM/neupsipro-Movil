package com.example.neupsipromovil.presentation.screens.login

import com.example.neupsipromovil.domain.model.Login

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val login: Login) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}