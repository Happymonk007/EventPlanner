package com.example.eventplanner.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val eventId: String,
    val createdAtEpochMillis: Long,
)

