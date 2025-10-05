package com.zulfa.eabsensi.core.util

import com.zulfa.eabsensi.core.data.remote.response.AnnouncementResponseItem
import com.zulfa.eabsensi.core.data.remote.response.HistoryAttendanceResponse
import com.zulfa.eabsensi.core.data.remote.response.HistoryAttendanceResponseItem
import com.zulfa.eabsensi.core.data.remote.response.LoginResponse
import com.zulfa.eabsensi.core.data.remote.response.MarkAttendanceResponse
import com.zulfa.eabsensi.core.data.remote.response.RequestLeaveResponse
import com.zulfa.eabsensi.core.data.remote.response.TodayAttendanceResponse
import com.zulfa.eabsensi.core.domain.model.AnnouncementDomain
import com.zulfa.eabsensi.core.domain.model.AttendanceDomain
import com.zulfa.eabsensi.core.domain.model.HistoryAttendanceDomain
import com.zulfa.eabsensi.core.domain.model.LoginDomain
import com.zulfa.eabsensi.core.domain.model.MarkAttendanceDomain
import com.zulfa.eabsensi.core.domain.model.MarkAttendanceResultDomain
import com.zulfa.eabsensi.core.domain.model.RequestLeaveDomain
import com.zulfa.eabsensi.core.domain.model.UserDomain

object DataMapper {

    fun mapLoginResponseToDomain(input: LoginResponse?): LoginDomain {
        return LoginDomain(
            message = input?.message.orEmpty(),
            token = input?.token.orEmpty(),
            user = UserDomain(
                id = input?.user?.id.orEmpty(),
                name = input?.user?.name.orEmpty(),
                username = input?.user?.username.orEmpty(),
                role = input?.user?.role.orEmpty()
            )
        )
    }

    fun mapTodayAttendanceResponseToDomain(input: TodayAttendanceResponse?): AttendanceDomain {
        return AttendanceDomain(
            date = input?.date.orEmpty(),
            createdAt = input?.createdAt.orEmpty(),
            userId = input?.userId.orEmpty(),
            mockedLocation = input?.mockedLocation ?: false,
            id = input?.id.orEmpty(),
            status = input?.status.orEmpty(),
            checkInTime = input?.checkInTime.orEmpty()
        )
    }

    fun mapHistoryAttendanceResponseToDomain(
        input: List<HistoryAttendanceResponseItem>?
    ): List<HistoryAttendanceDomain> =
        input?.map {
            HistoryAttendanceDomain(
                date = it.date.orEmpty(),
                userId = it.userId.orEmpty(),
                mockedLocation = it.mockedLocation ?: false,
                id = it.id.orEmpty(),
                status = it.status.orEmpty(),
                updatedAt = it.updatedAt.orEmpty(),
                checkInTime = it.checkInTime.orEmpty(),
                checkInLatitude = it.checkInLatitude ?: "",
                ipAddress = it.ipAddress.orEmpty(),
                checkInLongitude = it.checkInLongitude ?: "",
                androidId = it.androidId.orEmpty()
            )
        } ?: emptyList()

    fun mapMarkAttendanceResponseToDomain(input: MarkAttendanceResponse?): MarkAttendanceResultDomain {
        return MarkAttendanceResultDomain(
            message = input?.message.orEmpty(),
            data = MarkAttendanceDomain(
                date = input?.data?.date.orEmpty(),
                checkInTime = input?.data?.checkInTime.orEmpty(),
                mockedLocation = input?.data?.mockedLocation ?: false,
                checkInLatitude = input?.data?.checkInLatitude ?: 0.0,
                checkInLongitude = input?.data?.checkInLongitude ?: 0.0,
                ipAddress = input?.data?.ipAddress.orEmpty(),
                androidId = input?.data?.androidId.orEmpty(),
                status = input?.data?.status.orEmpty()
            )
        )
    }

    fun mapAnnouncementResponseToDomain(input: List<AnnouncementResponseItem>?): List<AnnouncementDomain> =
        input?.map{
            AnnouncementDomain(
                endDate = it.endDate.orEmpty(),
                id = it.id.orEmpty(),
                content = it.content.orEmpty(),
                startDate = it.startDate.orEmpty()
            )
        }?: emptyList()

    fun mapRequestLeaveResponseToDomain(response: RequestLeaveResponse?): RequestLeaveDomain {
        return RequestLeaveDomain(
            id = response?.data?.id ?: "",
            userId = response?.data?.userId ?: "",
            date = response?.data?.date ?: "",
            notes = response?.data?.notes ?: "",
            status = response?.data?.status ?: "",
            message = response?.message ?: ""
        )
    }
}