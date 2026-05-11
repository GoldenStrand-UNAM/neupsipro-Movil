package com.example.neupsipromovil.domain.usecase.user

import com.example.neupsipromovil.domain.model.UserProfile
import com.example.neupsipromovil.domain.repository.ProfileRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class GetUserProfileUseCaseTest {
    private lateinit var profileRepository: ProfileRepository
    private lateinit var getUserProfileUseCase: GetUserProfileUseCase

    @Before
    fun setup() {
        // Fresh mock before each test — no shared state between tests
        profileRepository = mock()
        getUserProfileUseCase = GetUserProfileUseCase(profileRepository)
    }

    // ─────────────────────────────────────────────
    // Happy path — valid userId returns a profile
    // ─────────────────────────────────────────────

    @Test
    fun `valid userId returns UserProfile success`() =
        runTest {
            // GIVEN: repository returns a valid profile for this UUID
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

            whenever(profileRepository.getUserProfile(uuid)).thenReturn(
                Result.success(fakeProfile),
            )

            val result = getUserProfileUseCase(uuid)

            // THEN: UseCase delegates correctly to repository and returns the profile
            assertTrue(result.isSuccess)
            assertEquals(fakeProfile, result.getOrNull())
        }

    // ─────────────────────────────────────────────
    // UC 1.3 — Profile not found
    // ─────────────────────────────────────────────

    @Test
    fun `UC 1-3 nonexistent userId returns NOT_FOUND failure`() =
        runTest {
            // GIVEN: repository signals that the profile does not exist
            val fakeUuid = "00000000-0000-0000-0000-000000000000"

            whenever(profileRepository.getUserProfile(fakeUuid)).thenReturn(
                Result.failure(Exception("NOT_FOUND")),
            )

            val result = getUserProfileUseCase(fakeUuid)

            // THEN: UseCase propagates the failure as-is — no transformation
            assertTrue(result.isFailure)
            assertEquals("NOT_FOUND", result.exceptionOrNull()?.message)
        }
}
