package com.zulfa.eabsensi.core.data.remote.response

import com.google.gson.annotations.SerializedName

typealias HistoryAttendanceResponse = List<HistoryAttendanceResponseItem>

data class HistoryAttendanceResponseItem(
	val date: String,
	val userId: String,
	val mockedLocation: Boolean,
	val id: String,
	val status: String,
	val updatedAt: String,

	@field:SerializedName("check_in_time")
	val checkInTime: String,
	val checkInLatitude: Any,
	val ipAddress: String,
	val checkInLongitude: Any,
	val androidId: String
)

