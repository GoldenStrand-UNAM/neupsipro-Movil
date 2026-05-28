package com.example.neupsipromovil.presentation.screens.profile

import com.example.neupsipromovil.domain.model.UserProfile
import com.example.neupsipromovil.domain.usecase.user.GetUserProfileUseCase
import com.example.neupsipromovil.domain.usecase.login.LogoutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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

class ProfileViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var getUserProfileUseCase: GetUserProfileUseCase
    private lateinit var logoutUseCase: LogoutUseCase
    private lateinit var viewModel: ProfileViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        // Replace Main dispatcher — JVM has no Main thread
        Dispatchers.setMain(testDispatcher)
        getUserProfileUseCase = mock()
        logoutUseCase = mock()
        viewModel = ProfileViewModel(getUserProfileUseCase, logoutUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    //
    // Verifies that the ViewModel starts in a clean state
    //

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `initial state has no user no error and is not loading`() {
        val state = viewModel.state.value

        // THEN: ViewModel starts clean before any getProfile call
        assertNull(state.user)
        assertNull(state.error)
        assertFalse(state.isLoading)
    }

    //
    // This test simulates a successful profile fetch:
    //

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getProfile success updates state with user profile`() =
        runTest {
            // GIVEN: UseCase returns a valid profile
            val uuid = "550e8400-e29b-41d4-a716-446655440000"
            val fakeProfile =
                UserProfile(
                    fullName = "John Doe",
                    profilePhoto = "https://example.com/photo.jpg",
                    age = 30,
                    stage = "Stand By",
                    neuroStatus = "Active",
                    registrationDate = "2024-01-15",
                    neuroEntryDate = "2024-01-20",
                    nextAppointmentDate = "2024-06-15",
                    nextAppointmentTime = "10:00 AM",
                    assignedClinic = "maria",
                    prosthetist = "Pedro",
                )

            // Mock function to return a successful result
            whenever(getUserProfileUseCase.invoke(any())).thenReturn(Result.success(fakeProfile))

            // WHEN: ViewModel is told to load the profile
            viewModel.getProfile(uuid)
            advanceUntilIdle()

            val state = viewModel.state.value

            // THEN: state has the profile, is not loading, has no error
            assertNotNull(state.user)
            assertEquals("John Doe", state.user?.fullName)
            assertFalse(state.isLoading)
            assertNull(state.error)
        }

    // ─────────────────────────────────────────────
    // UC 1.1 / 1.2 — Unauthorized — ViewModel stores error
    // ─────────────────────────────────────────────

    @Test
    fun `UC 1-1 and 1-2 UNAUTHORIZED error is stored in state`() =
        runTest {
            val uuid = "550e8400-e29b-41d4-a716-446655440000"

            whenever(getUserProfileUseCase(uuid)).thenReturn(
                Result.failure(Exception("UNAUTHORIZED")),
            )

            viewModel.getProfile(uuid)
            advanceUntilIdle()

            val state = viewModel.state.value

            // THEN: ViewModel stores the error — UI layer decides to redirect to login (UC 1.7)
            assertEquals("UNAUTHORIZED", state.error)
            assertNull(state.user)
            assertFalse(state.isLoading)
        }

    // ─────────────────────────────────────────────
    // UC 1.7 — Expired token clears session
    // ViewModel signals UNAUTHORIZED — UI reacts by clearing local session
    // ─────────────────────────────────────────────

    @Test
    fun `UC 1-7 expired token sets UNAUTHORIZED error and no user in state`() =
        runTest {
            val uuid = "550e8400-e29b-41d4-a716-446655440000"

            whenever(getUserProfileUseCase(uuid)).thenReturn(
                Result.failure(Exception("UNAUTHORIZED")),
            )

            viewModel.getProfile(uuid)
            advanceUntilIdle()

            val state = viewModel.state.value

            // THEN: user is null — no stale profile should remain visible
            assertNull(state.user)
            assertEquals("UNAUTHORIZED", state.error)
        }

    // ─────────────────────────────────────────────
    // UC 1.6 — No internet connection
    // ─────────────────────────────────────────────

    @Test
    fun `UC 1-6 NO_CONNECTION error is stored in state`() =
        runTest {
            val uuid = "550e8400-e29b-41d4-a716-446655440000"

            whenever(getUserProfileUseCase(uuid)).thenReturn(
                Result.failure(Exception("NO_CONNECTION")),
            )

            viewModel.getProfile(uuid)
            advanceUntilIdle()

            val state = viewModel.state.value

            // THEN: UI observes NO_CONNECTION and shows the offline message (UC 1.6.1)
            assertEquals("NO_CONNECTION", state.error)
            assertNull(state.user)
            assertFalse(state.isLoading)
        }

    // ─────────────────────────────────────────────
    // UC 2.1 — Profile with empty/null fields does not crash

    // ─────────────────────────────────────────────

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `UC 2-1 profile with empty fields updates state without crashing`() =
        runTest {
            val uuid = "550e8400-e29b-41d4-a716-446655440000"

            // GIVEN: Profile has minimal data — some fields empty, some null
            // The mapper already converted nulls to empty strings before reaching ViewModel
            val profileWithEmptyFields =
                UserProfile(
                    fullName = "",
                    profilePhoto = null,
                    age = 0,
                    stage = "",
                    neuroStatus = "",
                    registrationDate = "",
                    neuroEntryDate = "",
                    nextAppointmentDate = null,
                    nextAppointmentTime = null,
                    assignedClinic = "",
                    prosthetist = "",
                )

            // Mock the suspend function with proper syntax (invoke + any matcher)
            whenever(getUserProfileUseCase.invoke(any())).thenReturn(
                Result.success(profileWithEmptyFields),
            )

            // WHEN: ViewModel processes a profile with all empty fields
            viewModel.getProfile(uuid)
            advanceUntilIdle()

            val state = viewModel.state.value

            // THEN: state is success — no crash, no error (UC 2.1.2)
            // UI is responsible for displaying empty fields correctly (UC 2.1.1)
            assertNotNull(state.user)
            assertEquals("", state.user?.fullName)
            assertEquals("", state.user?.stage)
            assertNull(state.user?.profilePhoto)
            assertNull(state.error)
            assertFalse(state.isLoading)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `logout success triggers callback and update state`() = runTest {
        // GIVEN: logout useCase responds with success
        whenever(logoutUseCase.invoke()).thenReturn(Result.success(Unit))
        var callbackCalled = false

        //WHEN: Logging out
        viewModel.logout(onSuccessLogout = { callbackCalled = true})
        advanceUntilIdle()

        //THEN :callback executed & state changes correctly
        assertTrue(callbackCalled)
        assertFalse(viewModel.state.value.isLoading)
        assertNull(viewModel.state.value.error)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `logout failure stores error message in state`() = runTest {
        //GIVEN: UseCase fails with exception
        val errorMessage = "Error de red al cerrar sesión"
        whenever(logoutUseCase.invoke()).thenReturn(Result.failure(Exception(errorMessage)))
        var callbackCalled = false

        //WHEN: try logout
        viewModel.logout(onSuccessLogout = { callbackCalled = true})
        advanceUntilIdle()

        //THEN: Callback isnt called & error is stored in UI state
        assertFalse(callbackCalled)
        assertFalse(viewModel.state.value.isLoading)
        assertEquals(errorMessage, viewModel.state.value.error)
    }

}
