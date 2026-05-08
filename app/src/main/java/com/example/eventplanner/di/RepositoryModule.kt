package com.example.eventplanner.di

import com.example.eventplanner.data.repository.EventsRepositoryImpl
import com.example.eventplanner.data.repository.LocationRepositoryImpl
import com.example.eventplanner.domain.repository.EventsRepository
import com.example.eventplanner.domain.repository.LocationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindEventsRepository(impl: EventsRepositoryImpl): EventsRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(impl: LocationRepositoryImpl): LocationRepository
}

