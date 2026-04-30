package com.example.eventplanner.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventplanner.domain.model.Event
import com.example.eventplanner.domain.repository.EventsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val eventsRepository: EventsRepository,
) : ViewModel() {

    fun event(eventId: String): StateFlow<Event?> =
        eventsRepository.observeEvent(eventId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun setBookmarked(eventId: String, bookmarked: Boolean) {
        viewModelScope.launch {
            eventsRepository.setBookmarked(eventId, bookmarked)
        }
    }
}

