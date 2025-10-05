package com.zulfa.eabsensi.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class AnnouncementResponse(

	@field:SerializedName("AnnouncementResponse")
	val announcementResponse: List<AnnouncementResponseItem>
)

data class AnnouncementResponseItem(

	@field:SerializedName("end_date")
	val endDate: String,

	@field:SerializedName("_id")
	val id: String,
	
	@field:SerializedName("content")
	val content: String,

	@field:SerializedName("start_date")
	val startDate: String,

)
