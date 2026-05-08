package com.example.eventplanner.data.repository

import android.annotation.SuppressLint
import android.content.Context
import com.example.eventplanner.domain.model.UserLocation
import com.example.eventplanner.domain.repository.LocationRepository
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : LocationRepository {

    @SuppressLint("MissingPermission")
    override suspend fun getLastKnownLocation(): UserLocation? {
        val client = LocationServices.getFusedLocationProviderClient(context)
        val location = runCatching { client.lastLocation.await() }.getOrNull() ?: return null
        return UserLocation(latitude = location.latitude, longitude = location.longitude)
    }
}