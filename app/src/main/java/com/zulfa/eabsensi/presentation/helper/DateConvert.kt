package com.zulfa.eabsensi.presentation.helper

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateToIndonesian(dateString: String): String {
    return try {
        val localDate = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE) // yyyy-MM-dd
        val formatter = DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy", Locale("id", "ID"))
        localDate.format(formatter)
    } catch (e: Exception) {
        dateString // fallback kalau parsing gagal
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateToIndonesian2(dateString: String): String {
    return try {
        val instant = Instant.parse(dateString) // bisa parse "2025-09-29T16:00:00.000Z"
        val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("id", "ID"))
        localDate.format(formatter)
    } catch (e: Exception) {
            dateString
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getTodayDate(): String {
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy", Locale("id", "ID"))
    return today.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTimeToWITA(dateTimeString: String): String {
    return try {
        val zonedDateTime = ZonedDateTime.parse(dateTimeString) // UTC
        val witaTime = zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Makassar")) // WITA = UTC+8
        val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale("id", "ID"))
        witaTime.format(formatter)
    } catch (e: Exception) {
        dateTimeString
    }
}