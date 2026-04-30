package com.example.eventplanner.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE eventId = :eventId")
    suspend fun remove(eventId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE eventId = :eventId)")
    suspend fun isBookmarked(eventId: String): Boolean
}

