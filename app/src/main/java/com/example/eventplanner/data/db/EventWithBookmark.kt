package com.example.eventplanner.data.db

import androidx.room.Embedded

data class EventWithBookmark(
    @Embedded val event: EventEntity,
    val isBookmarked: Boolean,
)

