package com.example.eventplanner.domain.usecase

import com.example.eventplanner.domain.model.UserLocation
import com.example.eventplanner.domain.repository.LocationRepository
import javax.inject.Inject

class LocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
) {
    suspend fun getLastKnownLocation(): UserLocation? = locationRepository.getLastKnownLocation()
}

