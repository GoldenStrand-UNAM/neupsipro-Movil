package com.example.neupsipromovil.presentation.screens.profile

import com.example.neupsipromovil.domain.model.UserProfile

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: UserProfile? = null,
    val error: String? = null,
    val isLogoutSuccess: Boolean = false
)