package com.example.neupsipromovil.domain.usecase.login

import com.example.neupsipromovil.domain.repository.LoginRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.mock

class LoginUseCaseTest {
    // Mock of the repository — we control what it returns
    private lateinit var loginRepository: LoginRepository
    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setup() {
        // Create a fake repository before each test
        // This way each test starts clean with no shared state
        loginRepository = mock()
        loginUseCase = LoginUseCase(loginRepository)
    }

    // ─────────────────────────────────────────────
    // UC 2.1 — Empty fields
    // ─────────────────────────────────────────────

    @Test
    fun `UC 2-1 empty username returns EMPTY_FIELDS failure`() =
        runTest {
            // GIVEN: username is blank, password is valid
            val result = loginUseCase(username = "", password = "secret123")

            // THEN: UseCase must reject before calling the repository
            assertTrue(result.isFailure)
            assertEquals("EMPTY_FIELDS", result.exceptionOrNull()?.message)
        }

    @Test
    fun `UC 2-1 empty password returns EMPTY_FIELDS failure`() =
        runTest {
            // GIVEN: username is valid, password is blank
            val result = loginUseCase(username = "john", password = "   ")

            // THEN: blank strings (only spaces) are also considered empty
            assertTrue(result.isFailure)
            assertEquals("EMPTY_FIELDS", result.exceptionOrNull()?.message)
        }

    @Test
    fun `UC 2-1 both fields empty returns EMPTY_FIELDS failure`() =
        runTest {
            val result = loginUseCase(username = "", password = "")

            assertTrue(result.isFailure)
            assertEquals("EMPTY_FIELDS", result.exceptionOrNull()?.message)
        }
}
