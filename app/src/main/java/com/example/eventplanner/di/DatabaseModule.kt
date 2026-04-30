package com.example.eventplanner.di

import android.content.Context
import androidx.room.Room
import com.example.eventplanner.data.db.BookmarkDao
import com.example.eventplanner.data.db.EventDao
import com.example.eventplanner.data.db.EventPlannerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): EventPlannerDatabase =
        Room.databaseBuilder(context, EventPlannerDatabase::class.java, "event_planner.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideEventDao(db: EventPlannerDatabase): EventDao = db.eventDao()

    @Provides
    fun provideBookmarkDao(db: EventPlannerDatabase): BookmarkDao = db.bookmarkDao()
}

