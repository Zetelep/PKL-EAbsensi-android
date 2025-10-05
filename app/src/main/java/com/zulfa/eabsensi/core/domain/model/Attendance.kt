package com.zulfa.eabsensi.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AttendanceDomain(
    val date: String,
    val createdAt: String,
    val userId: String,
    val mockedLocation: Boolean,
    val id: String,
    val status: String,
    val checkInTime: String
): Parcelable


data class HistoryAttendanceDomain(
    val date: String,
    val userId: String,
    val mockedLocation: Boolean,
    val id: String,
    val status: String,
    val updatedAt: String,
    val checkInTime: String,
    val checkInLatitude: Any,
    val ipAddress: String,
    val checkInLongitude: Any,
    val androidId: String
)