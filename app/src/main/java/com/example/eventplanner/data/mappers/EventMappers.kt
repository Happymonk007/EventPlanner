package com.example.eventplanner.data.mappers

import com.example.eventplanner.data.api.EventDto
import com.example.eventplanner.data.db.EventEntity
import com.example.eventplanner.domain.model.Event

fun EventDto.toEntity(
    fetchedAtEpochMillis: Long,
    isBookmarked: Boolean,
): EventEntity =
    EventEntity(
        id = id,
        title = title,
        locationName = locationName,
        latitude = latitude,
        longitude = longitude,
        startTimeEpochMillis = startTimeEpochMillis,
        imageUrl = imageUrl,
        fetchedAtEpochMillis = fetchedAtEpochMillis,
        isBookmarked = isBookmarked,
    )

fun EventEntity.toDomain(): Event =
    Event(
        id = id,
        title = title,
        locationName = locationName,
        latitude = latitude,
        longitude = longitude,
        startTimeEpochMillis = startTimeEpochMillis,
        imageUrl = imageUrl,
        isBookmarked = isBookmarked,
    )

