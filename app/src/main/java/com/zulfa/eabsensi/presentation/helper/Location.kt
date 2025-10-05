package com.zulfa.eabsensi.presentation.helper

import android.content.Context
import android.location.Location
import android.os.Build
import com.google.android.gms.maps.model.LatLng

@Suppress("DEPRECATION")
fun Location.isMockLocation(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        this.isMock // API 31+
    } else {
        this.isFromMockProvider //  < 31
    }
}

fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
    return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
}

// Fungsi cek jarak
fun isInsideCircle(user: LatLng, officeLocation: LatLng, radius: Double): Boolean {
    val result = FloatArray(1)
    Location.distanceBetween(
        user.latitude, user.longitude,
        officeLocation.latitude, officeLocation.longitude,
        result
    )
    return result[0] <= radius
}