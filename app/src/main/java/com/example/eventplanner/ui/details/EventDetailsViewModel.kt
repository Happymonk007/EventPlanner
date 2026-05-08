package com.example.eventplanner.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventplanner.domain.model.Event
import com.example.eventplanner.domain.usecase.EventsUseCase
import com.example.eventplanner.ui.NAV_ARG_EVENT_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val eventsUseCase: EventsUseCase,
) : ViewModel() {

    private val eventId: String = checkNotNull(savedStateHandle[NAV_ARG_EVENT_ID])

    val event: StateFlow<Event?> =
        eventsUseCase.observeEvent(eventId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun setBookmarked(bookmarked: Boolean) {
        viewModelScope.launch {
            eventsUseCase.setBookmarked(eventId = eventId, bookmarked = bookmarked)
        }
    }
}

