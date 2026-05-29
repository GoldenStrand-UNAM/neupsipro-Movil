package com.example.neupsipromovil.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neupsipromovil.domain.usecase.login.LogoutUseCase
import com.example.neupsipromovil.domain.usecase.user.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    fun getProfile(userId: String) {
        println("DEBUG_VM: getProfile llamado con userId: '$userId'")
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = getUserProfileUseCase(userId)
            result.onSuccess { userProfile ->
                _state.update {
                    it.copy(user = userProfile, isLoading = false)
                }
            }.onFailure { exception ->
                _state.update {
                    it.copy(error = exception.message ?: "Error desconocido", isLoading = false)
                }
            }
        }
    }

    fun logout(onSuccessLogout: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result  = logoutUseCase()

            result.onSuccess {
                _state.update { it.copy(isLoading = false, isLogoutSuccess = true) }
                onSuccessLogout()
            }.onFailure { exception ->
                _state.update {
                    it.copy(error = exception.message ?: "Error al cerrar sesión", isLoading = false)
                }
            }
        }
    }
}