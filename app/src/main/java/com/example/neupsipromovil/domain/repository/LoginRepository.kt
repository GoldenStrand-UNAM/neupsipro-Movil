package com.example.neupsipromovil.domain.repository

import com.example.neupsipromovil.domain.model.Login

interface LoginRepository {
    suspend fun login(username: String, password: String): Result<Login>
}