package com.example.eventplanner.domain.repository

import com.example.eventplanner.domain.model.UserLocation

interface LocationRepository {
    suspend fun getLastKnownLocation(): UserLocation?
}

