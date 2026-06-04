package com.example.neupsipromovil.domain.repository

import com.example.neupsipromovil.domain.model.UserProfile

interface ProfileRepository {
    suspend fun getUserProfile(userId: String): Result<UserProfile>
}