package com.example.eventplanner.data.mappers

import com.example.eventplanner.data.api.EventDto
import com.example.eventplanner.data.db.EventEntity
import com.example.eventplanner.data.db.EventWithBookmark
import com.example.eventplanner.domain.model.Event

fun EventDto.toEntity(fetchedAtEpochMillis: Long): EventEntity =
    EventEntity(
        id = id,
        title = title,
        locationName = locationName,
        latitude = latitude,
        longitude = longitude,
        startTimeEpochMillis = startTimeEpochMillis,
        imageUrl = imageUrl,
        fetchedAtEpochMillis = fetchedAtEpochMillis,
    )

fun EventWithBookmark.toDomain(): Event =
    Event(
        id = event.id,
        title = event.title,
        locationName = event.locationName,
        latitude = event.latitude,
        longitude = event.longitude,
        startTimeEpochMillis = event.startTimeEpochMillis,
        imageUrl = event.imageUrl,
        isBookmarked = isBookmarked,
    )

