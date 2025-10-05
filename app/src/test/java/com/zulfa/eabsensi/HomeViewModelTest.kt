package com.zulfa.eabsensi

import com.zulfa.eabsensi.core.data.Resource
import com.zulfa.eabsensi.core.domain.model.AnnouncementDomain
import com.zulfa.eabsensi.core.domain.model.AttendanceDomain
import com.zulfa.eabsensi.core.domain.model.MarkAttendanceDomain
import com.zulfa.eabsensi.core.domain.model.MarkAttendanceResultDomain
import com.zulfa.eabsensi.core.domain.model.RequestLeaveDomain
import com.zulfa.eabsensi.core.domain.usecase.AuthUseCase
import com.zulfa.eabsensi.presentation.main.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var authUseCase: AuthUseCase
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        authUseCase = mock(AuthUseCase::class.java)
        viewModel = HomeViewModel(authUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAnnouncement success updates state`() = runTest {
        val fakeAnnouncements = listOf(
            AnnouncementDomain(
                "1", "Title", "Message",
                startDate = "2025-09-29"
            )
        )
        `when`(authUseCase.getAnnouncement())
            .thenReturn(flowOf(Resource.Success(fakeAnnouncements)))

        viewModel.getAnnouncement()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.announcementState.value
        assertTrue(state is Resource.Success && state.data == fakeAnnouncements)
        verify(authUseCase).getAnnouncement()
    }

    @Test
    fun `markAnnouncementShown sets flag to true`() {
        assertEquals(false, viewModel.hasShownAnnouncement)
        viewModel.markAnnouncementShown()
        assertEquals(true, viewModel.hasShownAnnouncement)
    }

    @Test
    fun `getTodayAttendance success caches attendance`() = runTest {
        val fakeAttendance = AttendanceDomain(
            "10", "2025-09-29", "Hadir",
            mockedLocation = false,
            id = "1",
            status = "Hadir",
            checkInTime = "08:30",
        )
        `when`(authUseCase.getTodayAttendance())
            .thenReturn(flowOf(Resource.Success(fakeAttendance)))

        viewModel.getTodayAttendance()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.todayAttendanceState.value
        assertTrue(state is Resource.Success && state.data == fakeAttendance)
        verify(authUseCase).getTodayAttendance()
    }

    @Test
    fun `requestLeave success updates state and refreshes attendance`() = runTest {
        val fakeAttendance = AttendanceDomain(
            "10", "2025-09-29", "Hadir",
            mockedLocation = false,
            id = "1",
            status = "Hadir",
            checkInTime = "08:30",

            )
        val fakeLeave = RequestLeaveDomain(
            id = "1",
            userId = "1",
            date = "2025-09-29",
            notes = "sakit",
            status = "Approved",
            message = "aa"
        )

        // cache currentAttendance
        `when`(authUseCase.getTodayAttendance())
            .thenReturn(flowOf(Resource.Success(fakeAttendance)))
        viewModel.getTodayAttendance()
        testDispatcher.scheduler.advanceUntilIdle()

        // mock requestLeave (harus "1" bukan "10")
        `when`(authUseCase.requestLeave("123", "1", "Sakit"))
            .thenReturn(flowOf(Resource.Success(fakeLeave)))

        viewModel.requestLeave("123", "Sakit")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.requestLeaveState.value
        assertTrue(state is Resource.Success && state.data == fakeLeave)

        verify(authUseCase).requestLeave("123", "1", "Sakit")
        verify(authUseCase, atLeastOnce()).getTodayAttendance()
    }

    @Test
    fun `markAttendance success updates state and refreshes attendance`() = runTest {
        val fakeAttendance = AttendanceDomain(
            "10", "2025-09-29", "Hadir",
            mockedLocation = false,
            id = "1",
            status = "Hadir",
            checkInTime = "08:30",

            )
        val fakeMark = MarkAttendanceResultDomain(
            "success",
            data = MarkAttendanceDomain(
                date = "2025-09-29",
                checkInTime = "08:30",
                mockedLocation = false,
                checkInLatitude = 100.0,
                checkInLongitude = 100.0,
                ipAddress = "1111",
                androidId = "1111",
                status = "Hadir"
            )
        )

        // cache currentAttendance
        `when`(authUseCase.getTodayAttendance())
            .thenReturn(flowOf(Resource.Success(fakeAttendance)))
        viewModel.getTodayAttendance()
        testDispatcher.scheduler.advanceUntilIdle()

        // mock markAttendance → harus pakai "1"
        `when`(authUseCase.markAttendance("123", "1", 1.23, 4.56, "device-1"))
            .thenReturn(flowOf(Resource.Success(fakeMark)))

        viewModel.markAttendance("123", 1.23, 4.56, "device-1")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.markAttendanceState.value
        assertTrue(state is Resource.Success && state.data == fakeMark)

        verify(authUseCase).markAttendance("123", "1", 1.23, 4.56, "device-1")
        verify(authUseCase, atLeastOnce()).getTodayAttendance()
    }


    @Test
    fun `requestLeave with null attendance does nothing`() = runTest {
        viewModel.requestLeave("123", "Sakit")
        testDispatcher.scheduler.advanceUntilIdle()

        // tetap Loading karena tidak ada currentAttendance
        assertTrue(viewModel.requestLeaveState.value is Resource.Loading)
        verify(authUseCase, never()).requestLeave(anyString(), anyString(), anyString())
    }

    @Test
    fun `markAttendance with null attendance does nothing`() = runTest {
        viewModel.markAttendance("123", 1.23, 4.56, "device-1")
        testDispatcher.scheduler.advanceUntilIdle()

        // tetap Loading karena tidak ada currentAttendance
        assertTrue(viewModel.markAttendanceState.value is Resource.Loading)
        verify(authUseCase, never()).markAttendance(anyString(), anyString(), anyDouble(), anyDouble(), anyString())
    }

    @Test
    fun `getAnnouncement error updates state`() = runTest {
        val errorMessage = "Failed to fetch announcements"
        `when`(authUseCase.getAnnouncement())
            .thenReturn(flowOf(Resource.Error(errorMessage)))

        viewModel.getAnnouncement()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.announcementState.value
        assertTrue(state is Resource.Error && state.message == errorMessage)
        verify(authUseCase).getAnnouncement()
    }

    @Test
    fun `getTodayAttendance error updates state`() = runTest {
        val errorMessage = "Failed to fetch attendance"
        `when`(authUseCase.getTodayAttendance())
            .thenReturn(flowOf(Resource.Error(errorMessage)))

        viewModel.getTodayAttendance()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.todayAttendanceState.value
        assertTrue(state is Resource.Error && state.message == errorMessage)
        verify(authUseCase).getTodayAttendance()
    }


}
