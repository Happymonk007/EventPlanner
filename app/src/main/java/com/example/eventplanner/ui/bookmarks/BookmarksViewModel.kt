package com.example.eventplanner.ui.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventplanner.domain.model.Event
import com.example.eventplanner.domain.usecase.EventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val eventsUseCase: EventsUseCase,
) : ViewModel() {

    val bookmarkedEvents: StateFlow<List<Event>> =
        eventsUseCase.observeBookmarkedEvents()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun setBookmarked(eventId: String, bookmarked: Boolean) {
        viewModelScope.launch {
            eventsUseCase.setBookmarked(eventId = eventId, bookmarked = bookmarked)
        }
    }
}

