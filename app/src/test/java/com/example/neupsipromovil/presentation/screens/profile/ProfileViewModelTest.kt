package com.example.neupsipromovil.presentation.screens.profile

import com.example.neupsipromovil.domain.model.UserProfile
import com.example.neupsipromovil.domain.usecase.user.GetUserProfileUseCase
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
    private lateinit var viewModel: ProfileViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        // Replace Main dispatcher — JVM has no Main thread
        Dispatchers.setMain(testDispatcher)
        getUserProfileUseCase = mock()
        viewModel = ProfileViewModel(getUserProfileUseCase)
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
                    unitEntryDate = "2024-01-15",
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
}
