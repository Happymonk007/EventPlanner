package com.example.eventplanner.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val id: String,
    val title: String,
    val locationName: String,
    val latitude: Double,
    val longitude: Double,
    val startTimeEpochMillis: Long,
    val imageUrl: String?,
    val fetchedAtEpochMillis: Long,
)

