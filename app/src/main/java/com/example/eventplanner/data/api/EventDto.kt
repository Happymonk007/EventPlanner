package com.example.eventplanner.data.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EventDto(
    val id: String,
    val title: String,
    val locationName: String,
    val latitude: Double,
    val longitude: Double,
    val startTimeEpochMillis: Long,
    val imageUrl: String?,
)

