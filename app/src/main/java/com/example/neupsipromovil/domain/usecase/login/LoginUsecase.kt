package com.example.neupsipromovil.domain.usecase.login

import com.example.neupsipromovil.domain.model.Login
import com.example.neupsipromovil.domain.repository.LoginRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke(username: String, password: String): Result<Login> {

        if (username.isBlank() || password.isBlank()) {
            return Result.failure(Exception("EMPTY_FIELDS"))
        }

        if (username.length > 30 || password.length > 30) {
            return Result.failure(Exception("INVALID_LENGTH"))
        }

        return loginRepository.login(username, password)
    }
}