package com.example.neupsipromovil.domain.usecase.login

import com.example.neupsipromovil.domain.repository.LoginRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class LogoutUseCaseTest {
    private lateinit var loginRepository: LoginRepository
    private lateinit var logoutUseCase: LogoutUseCase

    @Before
    fun setup() {
        loginRepository = mock()
        logoutUseCase = LogoutUseCase(loginRepository)
    }
    @Test
    fun `logout success returns success Unit`() =
        runTest {
            //GIVEN: repository where session was successfully destroyed
            whenever(loginRepository.logout()).thenReturn(Result.success(Unit))
            //WHEN: Execute usecase
            val result = logoutUseCase()
            //THEN: Confirm success
            assertTrue(result.isSuccess)
        }
    @Test
    fun `logout without internet returns NO_CONNECTION failure`() =
    runTest {
        whenever(loginRepository.logout()).thenReturn(
            Result.failure(Exception("NO_CONNECTION")),
        )
        //WHEN: Try logout
        val result = logoutUseCase()
        //THEN: Error conexion
        assertTrue(result.isFailure)
        assertEquals("NO_CONNECTION", result.exceptionOrNull()?.message)
    }
    @Test
    fun `logout server error returns SERVER_ERROR failure`() =
        runTest {
            //GIVEN: ServerError 500
            whenever(loginRepository.logout()).thenReturn(
                Result.failure(Exception("SERVER_ERROR")),
            )
            //WHEN: try logout
            val result = logoutUseCase()
            //THEN: Error Server
            assertTrue(result.isFailure)
            assertEquals("SERVER_ERROR", result.exceptionOrNull()?.message)
        }
}