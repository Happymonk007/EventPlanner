package com.example.eventplanner.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query(
        """
        SELECT e.*,
               CASE WHEN b.eventId IS NULL THEN 0 ELSE 1 END AS isBookmarked
        FROM events e
        LEFT JOIN bookmarks b ON b.eventId = e.id
        ORDER BY e.startTimeEpochMillis ASC
        """
    )
    fun observeEvents(): Flow<List<EventWithBookmark>>

    @Query(
        """
        SELECT e.*,
               CASE WHEN b.eventId IS NULL THEN 0 ELSE 1 END AS isBookmarked
        FROM events e
        LEFT JOIN bookmarks b ON b.eventId = e.id
        WHERE e.id = :eventId
        LIMIT 1
        """
    )
    fun observeEvent(eventId: String): Flow<EventWithBookmark?>

    @Query(
        """
        SELECT e.*,
               1 AS isBookmarked
        FROM events e
        INNER JOIN bookmarks b ON b.eventId = e.id
        ORDER BY e.startTimeEpochMillis ASC
        """
    )
    fun observeBookmarkedEvents(): Flow<List<EventWithBookmark>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(events: List<EventEntity>)

    @Query("SELECT MAX(fetchedAtEpochMillis) FROM events")
    suspend fun getLastFetchedAtEpochMillis(): Long?

    @Query("DELETE FROM events")
    suspend fun deleteAll()
}

