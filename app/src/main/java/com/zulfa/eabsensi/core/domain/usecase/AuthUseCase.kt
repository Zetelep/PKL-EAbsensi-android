package com.zulfa.eabsensi.core.domain.usecase

import com.zulfa.eabsensi.core.data.Resource
import com.zulfa.eabsensi.core.domain.model.AnnouncementDomain
import com.zulfa.eabsensi.core.domain.model.AttendanceDomain
import com.zulfa.eabsensi.core.domain.model.HistoryAttendanceDomain
import com.zulfa.eabsensi.core.domain.model.LoginDomain
import com.zulfa.eabsensi.core.domain.model.MarkAttendanceResultDomain
import com.zulfa.eabsensi.core.domain.model.RequestLeaveDomain
import kotlinx.coroutines.flow.Flow

interface AuthUseCase {

    fun login(username: String, password: String): Flow<Resource<LoginDomain>>

    fun getTodayAttendance(): Flow<Resource<AttendanceDomain>>

    fun getHistoryAttendance(): Flow<Resource<List<HistoryAttendanceDomain>>>

    fun markAttendance(
        userId: String,
        attendanceId: String,
        latitude: Double,
        longitude: Double,
        androidId: String
    ): Flow<Resource<MarkAttendanceResultDomain>>

    fun requestLeave(
        userId: String,
        attendanceId: String,
        notes: String
    ): Flow<Resource<RequestLeaveDomain>>


    fun getAnnouncement(): Flow<Resource<List<AnnouncementDomain>>>




}