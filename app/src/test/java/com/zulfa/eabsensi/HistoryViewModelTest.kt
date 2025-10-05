package com.zulfa.eabsensi

import com.zulfa.eabsensi.core.data.Resource
import com.zulfa.eabsensi.core.data.pref.UserPreference
import com.zulfa.eabsensi.core.domain.model.HistoryAttendanceDomain
import com.zulfa.eabsensi.core.domain.usecase.AuthUseCase
import com.zulfa.eabsensi.presentation.main.history.HistoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var authUseCase: AuthUseCase
    private lateinit var viewModel: HistoryViewModel
    private lateinit var userPreference: UserPreference


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        authUseCase = mock(AuthUseCase::class.java)
        userPreference = mock(UserPreference::class.java)
        viewModel = HistoryViewModel(authUseCase, userPreference)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAttendanceHistory success updates state with success`() = runTest {
        // Given
        val fakeHistory = listOf(
            HistoryAttendanceDomain(
                id = "1", date = "2025-09-29", status = "Hadir",
                userId = "1",
                mockedLocation = false,
                updatedAt = "2025-09-29",
                checkInTime = "09:30",
                checkInLatitude = -100.0,
                ipAddress = "1111",
                checkInLongitude = -100.0,
                androidId = "111"
            ),
            HistoryAttendanceDomain(
                id = "2", date = "2025-09-29", status = "Hadir",
                userId = "1",
                mockedLocation = false,
                updatedAt = "2025-09-30",
                checkInTime = "09:30",
                checkInLatitude = -100.0,
                ipAddress = "1111",
                checkInLongitude = -100.0,
                androidId = "111"
            )
        )
        `when`(authUseCase.getHistoryAttendance())
            .thenReturn(flowOf(Resource.Success(fakeHistory)))

        // When
        viewModel.getAttendanceHistory()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.historyState.value
        assertTrue(state is Resource.Success && state.data == fakeHistory)
        verify(authUseCase).getHistoryAttendance()
    }

    @Test
    fun `getAttendanceHistory error updates state with error`() = runTest {
        // Given
        val errorMessage = "Failed to fetch history"
        `when`(authUseCase.getHistoryAttendance())
            .thenReturn(flowOf(Resource.Error(errorMessage)))

        // When
        viewModel.getAttendanceHistory()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.historyState.value
        assertTrue(state is Resource.Error && state.message == errorMessage)
        verify(authUseCase).getHistoryAttendance()
    }

    @Test
    fun `initial state should be loading`() {
        // Then
        assertTrue(viewModel.historyState.value is Resource.Loading)
    }

    @Test
    fun `logout should call userPreference logout and update logoutState`() = runTest {
        // Given
        whenever(userPreference.logout()).thenReturn(Unit)

        // When
        viewModel.logout()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(userPreference).logout()
        assertTrue(viewModel.logoutState.value)
    }
}