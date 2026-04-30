package com.example.eventplanner.data.repository

import com.example.eventplanner.BuildConfig
import com.example.eventplanner.data.api.EventsApi
import com.example.eventplanner.data.assets.EventsAssetDataSource
import com.example.eventplanner.data.db.BookmarkDao
import com.example.eventplanner.data.db.BookmarkEntity
import com.example.eventplanner.data.db.EventDao
import com.example.eventplanner.data.mappers.toDomain
import com.example.eventplanner.data.mappers.toEntity
import com.example.eventplanner.domain.model.Event
import com.example.eventplanner.domain.repository.EventsRepository
import com.example.eventplanner.domain.repository.RefreshResult
import com.example.eventplanner.domain.util.CachePolicy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(
    private val api: EventsApi,
    private val eventsAssetDataSource: EventsAssetDataSource,
    private val eventDao: EventDao,
    private val bookmarkDao: BookmarkDao,
) : EventsRepository {

    override fun observeEvents(): Flow<List<Event>> =
        eventDao.observeEvents().map { list -> list.map { it.toDomain() } }

    override fun observeBookmarkedEvents(): Flow<List<Event>> =
        eventDao.observeBookmarkedEvents().map { list -> list.map { it.toDomain() } }

    override fun observeEvent(eventId: String): Flow<Event?> =
        eventDao.observeEvent(eventId).map { it?.toDomain() }

    override suspend fun refreshEventsIfStale(nowEpochMillis: Long): RefreshResult {
        val lastFetched = eventDao.getLastFetchedAtEpochMillis()
        val isStale = CachePolicy.isStale(
            lastFetchedAtEpochMillis = lastFetched,
            nowEpochMillis = nowEpochMillis,
            ttlMillis = BuildConfig.EVENTS_TTL_MS,
        )
        return if (isStale) forceRefresh(nowEpochMillis) else RefreshResult.Success
    }

    override suspend fun forceRefresh(nowEpochMillis: Long): RefreshResult {
        val dtos = runCatching {
            // Using @Url so this can be either a relative path or full URL.
            val urlOrPath = "${BuildConfig.EVENTS_BASE_URL}${BuildConfig.EVENTS_PATH}"
            api.getEvents(urlOrPath)
        }.getOrElse {
            // Fallback for offline demo / flaky networks.
            eventsAssetDataSource.readEventsFromAssets()
        }

        return runCatching {
            eventDao.upsertAll(dtos.map { it.toEntity(fetchedAtEpochMillis = nowEpochMillis) })
            RefreshResult.Success
        }.getOrElse { e ->
            RefreshResult.Failed(e.message ?: "Unknown persistence error")
        }
    }

    override suspend fun setBookmarked(eventId: String, bookmarked: Boolean, nowEpochMillis: Long) {
        if (bookmarked) {
            bookmarkDao.add(BookmarkEntity(eventId = eventId, createdAtEpochMillis = nowEpochMillis))
        } else {
            bookmarkDao.remove(eventId)
        }
    }
}

