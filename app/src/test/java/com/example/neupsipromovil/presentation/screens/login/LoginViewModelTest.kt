package com.example.neupsipromovil.presentation.screens.login

import com.example.neupsipromovil.domain.repository.LoginRepository
import com.example.neupsipromovil.domain.usecase.login.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class LoginViewModelTest {
    // Test dispatcher used to replace Dispatchers.Main during tests.
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var loginRepository: LoginRepository
    private lateinit var loginUseCase: LoginUseCase

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        // Replaces Dispatchers.Main with the test dispatcher.
        // This prevents crashes when coroutines try to use the Main thread in tests.
        Dispatchers.setMain(testDispatcher)

        // Create a fake/mock repository instead of using the real API
        loginRepository = mock()

        // Create the real use case with the mocked repository
        loginUseCase = LoginUseCase(loginRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        // Restores the original Main dispatcher after the test finishes
        Dispatchers.resetMain()
    }

    @Test
    fun `UC 3-2 after 5 failed attempts usecase returns failure each time`() =
        runTest {
            // GIVEN: repository always rejects the login
            whenever(loginRepository.login(any(), any())).thenReturn(
                Result.failure(Exception("INVALID_CREDENTIALS")),
            )

            // WHEN: simulate 5 consecutive failed attempts
            val results =
                (1..5).map {
                    loginUseCase(username = "john", password = "wrongpass")
                }

            // THEN: all 5 must be failures — the ViewModel layer handles the lockout counter
            assert(results.all { it.isFailure })
            assert(results.all { it.exceptionOrNull()?.message == "INVALID_CREDENTIALS" })
        }
}
