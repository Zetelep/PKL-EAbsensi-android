package com.zulfa.eabsensi.core.data

import com.zulfa.eabsensi.core.data.pref.UserModel
import com.zulfa.eabsensi.core.data.pref.UserPreference
import com.zulfa.eabsensi.core.data.remote.RemoteDataSource
import com.zulfa.eabsensi.core.data.remote.network.ApiResponse
import com.zulfa.eabsensi.core.data.remote.response.LeaveRequest
import com.zulfa.eabsensi.core.data.remote.response.MarkAttendanceRequest
import com.zulfa.eabsensi.core.domain.model.AnnouncementDomain
import com.zulfa.eabsensi.core.domain.model.AttendanceDomain
import com.zulfa.eabsensi.core.domain.model.HistoryAttendanceDomain
import com.zulfa.eabsensi.core.domain.model.LoginDomain
import com.zulfa.eabsensi.core.domain.model.MarkAttendanceResultDomain
import com.zulfa.eabsensi.core.domain.model.RequestLeaveDomain
import com.zulfa.eabsensi.core.domain.repository.IAuthRepository
import com.zulfa.eabsensi.core.util.DataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthRepository(private val remoteDataSource: RemoteDataSource,
                     private val userPreference: UserPreference
): IAuthRepository {
    override fun login(
        username: String,
        password: String
    ): Flow<Resource<LoginDomain>> {
        return flow {
            emit(Resource.Loading())
            remoteDataSource.login(username, password).collect { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Success -> {
                        val domainData = DataMapper.mapLoginResponseToDomain(apiResponse.data)

                        userPreference.saveSession(
                            UserModel(
                                name = domainData.user.name,
                                username = domainData.user.username,
                                id = domainData.user.id,
                                token = domainData.token,
                                isLogin = true
                            )
                        )

                        emit(Resource.Success(domainData))                    }
                    is ApiResponse.Empty -> {
                        emit(Resource.Error("No data available"))
                    }
                    is ApiResponse.Error -> {
                        emit(Resource.Error(apiResponse.errorMessage))
                    }
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getTodayAttendance(): Flow<Resource<AttendanceDomain>> {
        return flow {
            emit(Resource.Loading())

            val session = userPreference.getSession().first()
            val userId = session.id

            if (userId.isBlank()) {
                emit(Resource.Error("User ID not found"))
                return@flow
            }

            remoteDataSource.getTodayAttendance(userId).collect { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Success -> {
                        val domainData = DataMapper.mapTodayAttendanceResponseToDomain(apiResponse.data)
                        emit(Resource.Success(domainData))
                    }
                    is ApiResponse.Empty -> {
                        emit(Resource.Error("No data available"))
                    }
                    is ApiResponse.Error -> {
                        emit(Resource.Error(apiResponse.errorMessage))
                    }
                }
            }
        }
    }

    override fun getAttendanceHistory(): Flow<Resource<List<HistoryAttendanceDomain>>> {
        return flow {
            emit(Resource.Loading())

            val session = userPreference.getSession().first()
            val userId = session.id

            if (userId.isBlank()) {
                emit(Resource.Error("User ID not found"))
                return@flow
            }
            remoteDataSource.getAttendanceHistory(userId).collect { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Success -> {
                        val domainData = DataMapper.mapHistoryAttendanceResponseToDomain(apiResponse.data)
                        emit(Resource.Success(domainData))
                    }
                    is ApiResponse.Empty -> {
                        emit(Resource.Error("No data available"))
                    }
                    is ApiResponse.Error -> {
                        emit(Resource.Error(apiResponse.errorMessage))
                    }
                }
            }
        }
    }

    override fun markAttendance(
        userId: String,
        attendanceId: String,
        latitude: Double,
        longitude: Double,
        androidId: String
    ): Flow<Resource<MarkAttendanceResultDomain>> {
        return flow {
            emit(Resource.Loading())

            val request = MarkAttendanceRequest(
                latitude = latitude,
                longitude = longitude,
                androidId = androidId,
            )

            remoteDataSource.markAttendance(userId, attendanceId, request).collect { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Success -> {
                        val domainData = DataMapper.mapMarkAttendanceResponseToDomain(apiResponse.data)
                        emit(Resource.Success(domainData))
                    }
                    is ApiResponse.Empty -> {
                        emit(Resource.Error("No data available"))
                    }
                    is ApiResponse.Error -> {
                        emit(Resource.Error(apiResponse.errorMessage))
                    }
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun requestLeave(
        userId: String,
        attendanceId: String,
        notes: String
    ): Flow<Resource<RequestLeaveDomain>> {
        return flow {
            emit(Resource.Loading())

            val request = LeaveRequest(
                notes = notes
            )

            remoteDataSource.requestLeave(userId, attendanceId, request).collect { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Success -> {
                        val domainData = DataMapper.mapRequestLeaveResponseToDomain(apiResponse.data)
                        emit(Resource.Success(domainData))
                    }
                    is ApiResponse.Empty -> {
                        emit(Resource.Error("No data available"))
                    }
                    is ApiResponse.Error -> {
                        emit(Resource.Error(apiResponse.errorMessage))
                    }
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getAnnouncement(): Flow<Resource<List<AnnouncementDomain>>> {
        return flow {
            emit(Resource.Loading())

            remoteDataSource.getAnnouncement().collect { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Success -> {
                        val domainData = DataMapper.mapAnnouncementResponseToDomain(apiResponse.data)
                        emit(Resource.Success(domainData))
                    }
                    is ApiResponse.Empty -> {
                        emit(Resource.Error("No announcements available"))
                    }
                    is ApiResponse.Error -> {
                        emit(Resource.Error(apiResponse.errorMessage))
                    }
                }
            }
        }.flowOn(Dispatchers.IO)
    }
}