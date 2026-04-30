package com.example.eventplanner.domain.model

data class Event(
    val id: String,
    val title: String,
    val locationName: String,
    val latitude: Double,
    val longitude: Double,
    val startTimeEpochMillis: Long,
    val imageUrl: String?,
    val isBookmarked: Boolean,
)

