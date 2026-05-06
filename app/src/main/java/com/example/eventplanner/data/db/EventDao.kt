package com.example.eventplanner.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * FROM events ORDER BY startTimeEpochMillis ASC")
    fun observeEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE id = :eventId LIMIT 1")
    fun observeEvent(eventId: String): Flow<EventEntity?>

    @Query("SELECT * FROM events WHERE isBookmarked = 1 ORDER BY startTimeEpochMillis ASC")
    fun observeBookmarkedEvents(): Flow<List<EventEntity>>

    @Query("SELECT id FROM events WHERE isBookmarked = 1")
    suspend fun getBookmarkedIds(): List<String>

    @Query("UPDATE events SET isBookmarked = :bookmarked WHERE id = :eventId")
    suspend fun setBookmarked(eventId: String, bookmarked: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(events: List<EventEntity>)

    @Query("DELETE FROM events")
    suspend fun deleteAll()
}

