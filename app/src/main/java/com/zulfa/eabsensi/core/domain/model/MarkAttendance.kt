package com.zulfa.eabsensi.core.domain.model

data class MarkAttendanceResultDomain(
    val message: String,
    val data: MarkAttendanceDomain
)

data class MarkAttendanceDomain(
    val date: String,
    val checkInTime: String,
    val mockedLocation: Boolean,
    val checkInLatitude: Double,
    val checkInLongitude: Double,
    val ipAddress: String,
    val androidId: String,
    val status: String
)