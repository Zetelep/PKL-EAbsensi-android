package com.zulfa.eabsensi.core.domain.usecase

import com.zulfa.eabsensi.core.data.AuthRepository
import com.zulfa.eabsensi.core.data.Resource
import com.zulfa.eabsensi.core.domain.model.AnnouncementDomain
import com.zulfa.eabsensi.core.domain.model.AttendanceDomain
import com.zulfa.eabsensi.core.domain.model.HistoryAttendanceDomain
import com.zulfa.eabsensi.core.domain.model.LoginDomain
import com.zulfa.eabsensi.core.domain.model.MarkAttendanceResultDomain
import com.zulfa.eabsensi.core.domain.model.RequestLeaveDomain
import com.zulfa.eabsensi.core.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow

class AuthInteractor(private val authRepository: IAuthRepository): AuthUseCase {
    override fun login(
        username: String,
        password: String
    ): Flow<Resource<LoginDomain>> = authRepository.login(username, password)

    override fun getTodayAttendance(): Flow<Resource<AttendanceDomain>> = authRepository.getTodayAttendance()

    override fun getHistoryAttendance(): Flow<Resource<List<HistoryAttendanceDomain>>>  = authRepository.getAttendanceHistory()

    override fun markAttendance(
        userId: String,
        attendanceId: String,
        latitude: Double,
        longitude: Double,
        androidId: String
    ): Flow<Resource<MarkAttendanceResultDomain>>  = authRepository.markAttendance(userId, attendanceId, latitude, longitude, androidId)

    override fun requestLeave(
        userId: String,
        attendanceId: String,
        notes: String
    ): Flow<Resource<RequestLeaveDomain>> = authRepository.requestLeave(userId, attendanceId, notes)

    override fun getAnnouncement(): Flow<Resource<List<AnnouncementDomain>>> = authRepository.getAnnouncement()
}