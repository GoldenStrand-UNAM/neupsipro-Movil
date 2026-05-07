package com.example.neupsipromovil.domain.usecase.user

import com.example.neupsipromovil.domain.model.UserProfile
import com.example.neupsipromovil.domain.repository.ProfileRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(userId: String): Result<UserProfile> {
        return repository.getUserProfile(userId)
    }
}