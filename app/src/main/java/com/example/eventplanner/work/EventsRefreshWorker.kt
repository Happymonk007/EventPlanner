package com.example.eventplanner.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.eventplanner.domain.repository.EventsRepository
import com.example.eventplanner.domain.repository.RefreshResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class EventsRefreshWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val eventsRepository: EventsRepository,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return when (val result = eventsRepository.forceRefresh()) {
            is RefreshResult.Success -> Result.success()
            is RefreshResult.Failed -> Result.retry()
        }
    }
}

