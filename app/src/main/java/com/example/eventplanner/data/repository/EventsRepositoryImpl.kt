package com.example.eventplanner.data.repository

import com.example.eventplanner.BuildConfig
import com.example.eventplanner.data.api.EventsApi
import com.example.eventplanner.data.assets.EventsAssetDataSource
import com.example.eventplanner.data.db.EventDao
import com.example.eventplanner.data.mappers.toDomain
import com.example.eventplanner.data.mappers.toEntity
import com.example.eventplanner.domain.model.Event
import com.example.eventplanner.domain.repository.EventsRepository
import com.example.eventplanner.domain.repository.RefreshResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(
    private val api: EventsApi,
    private val eventsAssetDataSource: EventsAssetDataSource,
    private val eventDao: EventDao,
) : EventsRepository {

    override fun observeEvents(): Flow<List<Event>> =
        eventDao.observeEvents().map { list -> list.map { it.toDomain() } }

    override fun observeBookmarkedEvents(): Flow<List<Event>> =
        eventDao.observeBookmarkedEvents().map { list -> list.map { it.toDomain() } }

    override fun observeEvent(eventId: String): Flow<Event?> =
        eventDao.observeEvent(eventId).map { it?.toDomain() }

    override suspend fun loadEvents(nowEpochMillis: Long): RefreshResult {
        return withContext(Dispatchers.IO) {
            val previouslyBookmarkedIds = eventDao.getBookmarkedIds().toSet()
            val dtos = runCatching {
                val urlOrPath = "${BuildConfig.EVENTS_BASE_URL}${BuildConfig.EVENTS_PATH}"
                api.getEvents(urlOrPath)
            }.getOrElse {
                eventsAssetDataSource.readEventsFromAssets()
            }

            runCatching {
                eventDao.deleteAll()
                eventDao.upsertAll(
                    dtos.map { dto ->
                        dto.toEntity(
                            fetchedAtEpochMillis = nowEpochMillis,
                            isBookmarked = previouslyBookmarkedIds.contains(dto.id),
                        )
                    },
                )
                RefreshResult.Success
            }.getOrElse { e ->
                RefreshResult.Failed(e.message ?: "Unknown persistence error")
            }
        }
    }

    override suspend fun setBookmarked(eventId: String, bookmarked: Boolean, nowEpochMillis: Long) {
        eventDao.setBookmarked(eventId, bookmarked)
    }
}

