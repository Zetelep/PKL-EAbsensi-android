package com.zulfa.eabsensi.core.data.remote

import android.util.Log
import com.zulfa.eabsensi.core.data.remote.network.ApiResponse
import com.zulfa.eabsensi.core.data.remote.network.ApiService
import com.zulfa.eabsensi.core.data.remote.response.AnnouncementResponseItem
import com.zulfa.eabsensi.core.data.remote.response.HistoryAttendanceResponseItem
import com.zulfa.eabsensi.core.data.remote.response.LeaveRequest
import com.zulfa.eabsensi.core.data.remote.response.LoginRequest
import com.zulfa.eabsensi.core.data.remote.response.LoginResponse
import com.zulfa.eabsensi.core.data.remote.response.MarkAttendanceRequest
import com.zulfa.eabsensi.core.data.remote.response.MarkAttendanceResponse
import com.zulfa.eabsensi.core.data.remote.response.RequestLeaveResponse
import com.zulfa.eabsensi.core.data.remote.response.TodayAttendanceResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import retrofit2.HttpException

class RemoteDataSource(private val apiService: ApiService) {
     fun login(username: String, password: String): Flow<ApiResponse<LoginResponse>> {
        return flow {
            try {
                val request = LoginRequest(username, password)
                val response = apiService.login(request) // suspend function

                // If response contains token, consider success
                if (response.token.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }

            } catch (e: HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    JSONObject(errorBody ?: "").getString("message")
                }catch (ex: Exception) {
                    e.message()
                }
                emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
                Log.e("RemoteDataSource", errorMessage ?: e.toString())
            }
            catch (e: Exception) {
                val errorMessage = when (e) {
                    is java.net.UnknownHostException ->
                        "Koneksi internet atau server bermasalah, silakan periksa koneksi internet Anda. Jika masalah ini terus berlanjut, hubungi Admin."
                    is java.net.SocketTimeoutException ->
                        "Koneksi terputus. Silakan periksa koneksi internet Anda"
                    else -> e.localizedMessage ?: "Unexpected error occurred."
                }
                emit(ApiResponse.Error(errorMessage))
                Log.e("RemoteDataSource", errorMessage)
            }
        }.flowOn(Dispatchers.IO)

    }

    fun getTodayAttendance(userId: String): Flow<ApiResponse<TodayAttendanceResponse>> {
        return flow {
            try {
                val response = apiService.getTodayAttendance(userId)
                emit(ApiResponse.Success(response))
            } catch (e: HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    JSONObject(errorBody ?: "").getString("message")
                }catch (ex: Exception) {
                    e.message()
                }
                emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
                Log.e("RemoteDataSource", errorMessage ?: e.toString())
            }
            catch (e: Exception) {
                val errorMessage = when (e) {
                    is java.net.UnknownHostException ->
                        "Internet or server error, please check your internet. If this keeps happening, contact the Admin."
                    is java.net.SocketTimeoutException ->
                        "Connection timed out. Please check your internet connection."
                    else -> e.localizedMessage ?: "Unexpected error occurred."
                }
                emit(ApiResponse.Error(errorMessage))
                Log.e("RemoteDataSource", errorMessage)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getAttendanceHistory(userId: String): Flow<ApiResponse<List<HistoryAttendanceResponseItem>>> {
        return flow {
            try {
                val response = apiService.getAttendanceHistory(userId)
                val dataArray = response

                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                }else{
                    emit(ApiResponse.Empty)
                }
            }catch (e: HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    JSONObject(errorBody ?: "").getString("message")
                }catch (ex: Exception) {
                    e.message()
                }
                emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
                Log.e("RemoteDataSource", errorMessage ?: e.toString())
            }
            catch (e: Exception) {
                val errorMessage = when (e) {
                    is java.net.UnknownHostException ->
                        "Internet or server error, please check your internet. If this keeps happening, contact the Admin."
                    is java.net.SocketTimeoutException ->
                        "Connection timed out. Please check your internet connection."
                    else -> e.localizedMessage ?: "Unexpected error occurred."
                }
                emit(ApiResponse.Error(errorMessage))
                Log.e("RemoteDataSource", errorMessage)
            }
        }.flowOn(Dispatchers.IO)

    }

    fun markAttendance(
        userId: String,
        attendanceId: String,
        request: MarkAttendanceRequest
    ): Flow<ApiResponse<MarkAttendanceResponse>> {
        return flow {
            try {
                val response = apiService.markAttendance(userId, attendanceId, request)

                if (response.message.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    JSONObject(errorBody ?: "").getString("message")
                }catch (ex: Exception) {
                    e.message()
                }
                emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
                Log.e("RemoteDataSource", errorMessage ?: e.toString())
            }
            catch (e: Exception) {
                val errorMessage = when (e) {
                    is java.net.UnknownHostException ->
                        "Internet or server error, please check your internet. If this keeps happening, contact the Admin."
                    is java.net.SocketTimeoutException ->
                        "Connection timed out. Please check your internet connection."
                    else -> e.localizedMessage ?: "Unexpected error occurred."
                }
                emit(ApiResponse.Error(errorMessage))
                Log.e("RemoteDataSource", errorMessage)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun requestLeave(
        userId: String,
        attendanceId: String,
        request: LeaveRequest
    ): Flow<ApiResponse<RequestLeaveResponse>> {
        return flow {
            try {
                val response = apiService.requestLeave(userId, attendanceId,request) // suspend function

                // If response contains token, consider success
                if (response.message.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }

            }catch (e: HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    JSONObject(errorBody ?: "").getString("message")
                }catch (ex: Exception) {
                    e.message()
                }
                emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
                Log.e("RemoteDataSource", errorMessage ?: e.toString())
            }
            catch (e: Exception) {
                val errorMessage = when (e) {
                    is java.net.UnknownHostException ->
                        "Internet or server error, please check your internet. If this keeps happening, contact the Admin."
                    is java.net.SocketTimeoutException ->
                        "Connection timed out. Please check your internet connection."
                    else -> e.localizedMessage ?: "Unexpected error occurred."
                }
                emit(ApiResponse.Error(errorMessage))
                Log.e("RemoteDataSource", errorMessage)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getAnnouncement(): Flow<ApiResponse<List<AnnouncementResponseItem>>> {
        return flow {
            try {
                val response = apiService.getActiveAnnouncement()

                if (response.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    JSONObject(errorBody ?: "").getString("message")
                }catch (ex: Exception) {
                    e.message()
                }
                emit(ApiResponse.Error(errorMessage ?: "Unknown error"))
                Log.e("RemoteDataSource", errorMessage ?: e.toString())
            }
            catch (e: Exception) {
                val errorMessage = when (e) {
                    is java.net.UnknownHostException ->
                        "Internet or server error, please check your internet. If this keeps happening, contact the Admin."
                    is java.net.SocketTimeoutException ->
                        "Connection timed out. Please check your internet connection."
                    else -> e.localizedMessage ?: "Unexpected error occurred."
                }
                emit(ApiResponse.Error(errorMessage))
                Log.e("RemoteDataSource", errorMessage)
            }
        }.flowOn(Dispatchers.IO)
    }

}

