package com.example.neupsipromovil.domain.usecase.login

import com.example.neupsipromovil.domain.model.Login
import com.example.neupsipromovil.domain.repository.LoginRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

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

    // ─────────────────────────────────────────────
    // UC 2.3 — Character limit (max 30)
    // ─────────────────────────────────────────────

    @Test
    fun `UC 2-3 username longer than 30 chars returns INVALID_LENGTH`() =
        runTest {
            // GIVEN: a string with 31 characters
            val longUsername = "a".repeat(31)

            val result = loginUseCase(username = longUsername, password = "secret")

            // THEN: UseCase catches it before the network call
            assertTrue(result.isFailure)
            assertEquals("INVALID_LENGTH", result.exceptionOrNull()?.message)
        }

    @Test
    fun `UC 2-3 pasting 10000 chars in username returns INVALID_LENGTH`() =
        runTest {
            // GIVEN: user pastes 10,000 chars (UC 2.3.1)
            val hugeInput = "a".repeat(10_000)

            val result = loginUseCase(username = hugeInput, password = "secret")

            assertTrue(result.isFailure)
            assertEquals("INVALID_LENGTH", result.exceptionOrNull()?.message)
        }

    @Test
    fun `UC 2-3 password longer than 30 chars returns INVALID_LENGTH`() =
        runTest {
            val longPassword = "p".repeat(31)

            val result = loginUseCase(username = "john", password = longPassword)

            assertTrue(result.isFailure)
            assertEquals("INVALID_LENGTH", result.exceptionOrNull()?.message)
        }

    // ─────────────────────────────────────────────
    // UC 1.1 — SQL Injection treated as plain string
    // ─────────────────────────────────────────────

    @Test
    fun `UC 1-1 SQL injection string is treated as plain text and forwarded`() =
        runTest {
            // GIVEN: classic SQL injection attempt

            val sqlPayload = "' OR '1'='1"
            val fakeLogin = Login(username = sqlPayload)

            // Teach the mock: when repository receives this exact input, return success
            whenever(loginRepository.login(sqlPayload, "password")).thenReturn(
                Result.success(fakeLogin),
            )

            val result = loginUseCase(username = sqlPayload, password = "password")

            // THEN: UseCase passes it through — it is just a string, not a threat at this layer
            assertTrue(result.isSuccess)
            assertEquals(fakeLogin, result.getOrNull())
        }

    // ─────────────────────────────────────────────
    // UC 1.2 — XSS treated as plain string
    // ─────────────────────────────────────────────

    @Test
    fun `UC 1-2 XSS script tag is treated as plain text and forwarded`() =
        runTest {
            // GIVEN: XSS injection attempt via username field
            val xssPayload = "<script>alert('xss')</script>"
            val fakeLogin = Login(username = xssPayload)

            whenever(loginRepository.login(xssPayload, "pass")).thenReturn(
                Result.success(fakeLogin),
            )

            val result = loginUseCase(username = xssPayload, password = "pass")

            // THEN: treated as a normal string — no special blocking at UseCase level
            assertTrue(result.isSuccess)
        }
}
