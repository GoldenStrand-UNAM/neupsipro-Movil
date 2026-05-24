package com.example.neupsipromovil.domain.usecase.login

import com.example.neupsipromovil.domain.model.Login
import com.example.neupsipromovil.domain.repository.LoginRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.logout()
    }
}
