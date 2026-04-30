package com.example.eventplanner.ui.util

import android.location.Location
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

fun formatEpochMillis(epochMillis: Long): String {
    val formatter = SimpleDateFormat("EEE, d MMM • h:mm a", Locale.getDefault())
    return formatter.format(Date(epochMillis))
}

fun distanceMeters(
    fromLat: Double,
    fromLng: Double,
    toLat: Double,
    toLng: Double,
): Float {
    val results = FloatArray(1)
    Location.distanceBetween(fromLat, fromLng, toLat, toLng, results)
    return results[0]
}

fun formatDistance(meters: Float): String {
    return if (meters < 1000f) {
        "${meters.roundToInt()} m"
    } else {
        val km = meters / 1000f
        String.format(Locale.getDefault(), "%.1f km", km)
    }
}

