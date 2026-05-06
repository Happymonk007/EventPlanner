package com.example.eventplanner.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        EventEntity::class,
    ],
    version = 2,
    exportSchema = true,
)
abstract class EventPlannerDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}

