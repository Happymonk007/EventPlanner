package com.example.eventplanner.domain.repository

import com.example.eventplanner.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventsRepository {
    fun observeEvents(): Flow<List<Event>>
    fun observeBookmarkedEvents(): Flow<List<Event>>
    fun observeEvent(eventId: String): Flow<Event?>

    suspend fun refreshEventsIfStale(nowEpochMillis: Long = System.currentTimeMillis()): RefreshResult
    suspend fun forceRefresh(nowEpochMillis: Long = System.currentTimeMillis()): RefreshResult

    suspend fun setBookmarked(eventId: String, bookmarked: Boolean, nowEpochMillis: Long = System.currentTimeMillis())
}

sealed interface RefreshResult {
    data object Success : RefreshResult
    data class Failed(val reason: String) : RefreshResult
}

