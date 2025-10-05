package com.zulfa.eabsensi.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class TodayAttendanceResponse(
	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("mocked_location")
	val mockedLocation: Boolean,

	@field:SerializedName("_id")
	val id: String,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("check_in_time")
	val checkInTime: String,

)

