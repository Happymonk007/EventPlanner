package com.example.eventplanner.domain.usecase

import com.example.eventplanner.domain.model.Event
import com.example.eventplanner.domain.repository.EventsRepository
import com.example.eventplanner.domain.repository.RefreshResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventsUseCase @Inject constructor(
    private val eventsRepository: EventsRepository,
) {
    fun observeEvents(): Flow<List<Event>> = eventsRepository.observeEvents()

    fun observeBookmarkedEvents(): Flow<List<Event>> = eventsRepository.observeBookmarkedEvents()

    fun observeEvent(eventId: String): Flow<Event?> = eventsRepository.observeEvent(eventId)

    suspend fun loadEvents(nowEpochMillis: Long = System.currentTimeMillis()): RefreshResult =
        eventsRepository.loadEvents(nowEpochMillis)

    suspend fun setBookmarked(
        eventId: String,
        bookmarked: Boolean,
        nowEpochMillis: Long = System.currentTimeMillis(),
    ) {
        eventsRepository.setBookmarked(
            eventId = eventId,
            bookmarked = bookmarked,
            nowEpochMillis = nowEpochMillis,
        )
    }
}

