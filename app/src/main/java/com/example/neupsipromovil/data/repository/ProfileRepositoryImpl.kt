package com.example.neupsipromovil.data.repository

import com.example.neupsipromovil.data.remote.api.APIService
import com.example.neupsipromovil.domain.mapper.toDomain
import com.example.neupsipromovil.domain.repository.ProfileRepository
import com.example.neupsipromovil.domain.model.UserProfile
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiService: APIService
) : ProfileRepository {

    override suspend fun getUserProfile(userId: String): Result<UserProfile> {
        return try {
            val response = apiService.getUserProfile(userId)
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}