package com.zulfa.eabsensi.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class MarkAttendanceResponse(

	@field:SerializedName("data")
	val data: MarkAttendanceResponseItem,

	@field:SerializedName("message")
	val message: String
)

data class MarkAttendanceResponseItem(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("check_in_time")
	val checkInTime: String,

	@field:SerializedName("mocked_location")
	val mockedLocation: Boolean,

	@field:SerializedName("check_in_latitude")
	val checkInLatitude: Double,

	@field:SerializedName("ip_address")
	val ipAddress: String,

	@field:SerializedName("check_in_longitude")
	val checkInLongitude: Double,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("_id")
	val id: String,

	@field:SerializedName("android_id")
	val androidId: String,

	@field:SerializedName("status")
	val status: String,

)

data class MarkAttendanceRequest(
	val latitude: Double,
	val longitude: Double,

	@field:SerializedName("android_id")
	val androidId: String
)
