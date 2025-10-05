package com.zulfa.eabsensi.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class RequestLeaveResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String
)

data class Data(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("notes")
	val notes: String,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("_id")
	val id: String,

	@field:SerializedName("status")
	val status: String,
)

data class LeaveRequest(
    val notes: String
)
