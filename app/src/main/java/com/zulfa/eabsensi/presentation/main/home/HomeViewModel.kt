package com.zulfa.eabsensi.presentation.main.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zulfa.eabsensi.core.data.Resource
import com.zulfa.eabsensi.core.domain.model.AnnouncementDomain
import com.zulfa.eabsensi.core.domain.model.AttendanceDomain
import com.zulfa.eabsensi.core.domain.model.MarkAttendanceResultDomain
import com.zulfa.eabsensi.core.domain.model.RequestLeaveDomain
import com.zulfa.eabsensi.core.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val authUseCase: AuthUseCase
): ViewModel() {
    private val _announcementState = MutableStateFlow<Resource<List<AnnouncementDomain>>>(Resource.Loading())
    val announcementState: StateFlow<Resource<List<AnnouncementDomain>>> = _announcementState

    private val _requestLeaveState = MutableStateFlow<Resource<RequestLeaveDomain>>(Resource.Loading())
    val requestLeaveState: StateFlow<Resource<RequestLeaveDomain>> = _requestLeaveState

    private val _todayAttendanceState = MutableStateFlow<Resource<AttendanceDomain>>(Resource.Loading())
    val todayAttendanceState: StateFlow<Resource<AttendanceDomain>> = _todayAttendanceState

    private val _markAttendanceState = MutableStateFlow<Resource<MarkAttendanceResultDomain>>(Resource.Loading())
    val markAttendanceState: StateFlow<Resource<MarkAttendanceResultDomain>> = _markAttendanceState

    private var currentAttendance: AttendanceDomain? = null


    var hasShownAnnouncement = false
        private set

    fun getAnnouncement() {
//        Log.e("HomeViewModel", "Fetching announcements...")
        viewModelScope.launch {
            authUseCase.getAnnouncement()
                .collect { result ->
                    _announcementState.value = result
                }
        }
    }

    fun markAnnouncementShown() {
        hasShownAnnouncement = true
//        Log.e("HomeViewModel", "Announcement marked as shown")
    }

    fun requestLeave(
        userId: String,
        notes: String
    ) {
        val attendanceId = currentAttendance?.id
//        Log.d("UI", "userId=$userId attendanceId=$attendanceId")

        if (attendanceId.isNullOrEmpty()) {
//            Log.e("UI", "Attendance ID is null, cannot mark attendance")
            return
        }
//        Log.d("UI", "userId=$userId attendanceId=$attendanceId")

        viewModelScope.launch {

            authUseCase.requestLeave(userId, attendanceId, notes)
                .collect { result ->
                    _requestLeaveState.value = result

                    if (result is Resource.Success) {
                        getTodayAttendance()
                    }
                }
        }
    }

    fun getTodayAttendance() {
//        Log.d("HomeViewModel", "Fetching today's attendance...")

        viewModelScope.launch {
            authUseCase.getTodayAttendance()
                .collect { result ->
                    _todayAttendanceState.value = result

                    // ✅ cache the attendance if success
                    if (result is Resource.Success) {
                        currentAttendance = result.data
//                        Log.d("HomeViewModel", "Cached currentAttendance: $currentAttendance")

                    }
                }
        }
    }

    fun markAttendance(
        userId: String,
        latitude: Double,
        longitude: Double,
        androidId: String
    ) {
        val attendanceId = currentAttendance?.id
//        Log.d("UI", "userId=$userId attendanceId=$attendanceId")

        if (attendanceId.isNullOrEmpty()) {
//            Log.e("UI", "Attendance ID is null, cannot mark attendance")
            return
        }
//        Log.d("UI", "userId=$userId attendanceId=$attendanceId")
        viewModelScope.launch {
            authUseCase.markAttendance(userId, attendanceId, latitude, longitude, androidId)
                .collect { result ->
                    _markAttendanceState.value = result

                    if (result is Resource.Success) {
                        getTodayAttendance()
                    }
                }
        }
    }

    fun clearMarkAttendanceState() {
        _markAttendanceState.value = Resource.Loading()
    }

    fun clearRequestLeaveState() {
        _requestLeaveState.value = Resource.Loading()
    }
}