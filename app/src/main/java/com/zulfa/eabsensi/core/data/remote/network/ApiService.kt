package com.zulfa.eabsensi.core.data.remote.network

import com.zulfa.eabsensi.core.data.remote.response.AnnouncementResponse
import com.zulfa.eabsensi.core.data.remote.response.AnnouncementResponseItem
import com.zulfa.eabsensi.core.data.remote.response.HistoryAttendanceResponse
import com.zulfa.eabsensi.core.data.remote.response.LeaveRequest
import com.zulfa.eabsensi.core.data.remote.response.LoginRequest
import com.zulfa.eabsensi.core.data.remote.response.LoginResponse
import com.zulfa.eabsensi.core.data.remote.response.MarkAttendanceRequest
import com.zulfa.eabsensi.core.data.remote.response.MarkAttendanceResponse
import com.zulfa.eabsensi.core.data.remote.response.RequestLeaveResponse
import com.zulfa.eabsensi.core.data.remote.response.TodayAttendanceResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {


    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @GET("users/{userId}/attendance/today")
    suspend fun getTodayAttendance(
        @Path("userId") userId: String
    ): TodayAttendanceResponse

    @GET("users/{userId}/attendance")
    suspend fun getAttendanceHistory(
        @Path("userId") userId: String
    ): HistoryAttendanceResponse

    // POST /api/users/:userId/attendance/:attendanceId
    @POST("users/{userId}/attendance/{attendanceId}")
    suspend fun markAttendance(
        @Path("userId") userId: String,
        @Path("attendanceId") attendanceId: String,
        @Body request: MarkAttendanceRequest
    ): MarkAttendanceResponse

    @GET("announcements/active")
    suspend fun getActiveAnnouncement(): List<AnnouncementResponseItem>

    @PUT("users/{userId}/attendance/{attendanceId}/request-leave")
    suspend fun requestLeave(
        @Path("userId") userId: String,
        @Path("attendanceId") attendanceId: String,
        @Body request: LeaveRequest
    ): RequestLeaveResponse



}