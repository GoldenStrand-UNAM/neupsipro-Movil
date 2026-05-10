package com.example.neupsipromovil.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neupsipromovil.data.local.AuthManager
import com.example.neupsipromovil.domain.usecase.login.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    authManager: AuthManager
) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean> = authManager.sessionState

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    fun onUsernameChanged(value: String) { _username.value = value }
    fun onPasswordChanged(value: String) { _password.value = value }

    fun login() {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading

            val result = loginUseCase(_username.value.trim(), _password.value)

            result.fold(
                onSuccess = { login ->
                    _loginState.value = LoginUiState.Success(login)
                },
                onFailure = { error ->
                    _loginState.value = LoginUiState.Error(mapError(error.message))
                }
            )
        }
    }

    private fun mapError(code: String?): String = when (code) {
        "EMPTY_FIELDS" -> "El usuario y la contraseña son obligatorios"
        "INVALID_LENGTH" -> "Máximo 30 caracteres"
        "INVALID_CREDENTIALS" -> "Credenciales inválidas"
        "USER_DISABLED" -> "Esta cuenta no esta  desactivada"
        "TOO_MANY_REQUESTS" -> "Demasiados intentos. Intenta más tarde"
        "NETWORK_ERROR" -> "Error de conexión"
        else -> "Error inesperado"
    }

    fun resetState() { _loginState.value = LoginUiState.Idle }
}