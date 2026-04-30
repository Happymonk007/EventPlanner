package com.example.eventplanner.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventplanner.domain.model.Event
import com.example.eventplanner.domain.model.UserLocation
import com.example.eventplanner.domain.repository.EventsRepository
import com.example.eventplanner.domain.repository.LocationRepository
import com.example.eventplanner.domain.repository.RefreshResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val eventsRepository: EventsRepository,
    private val locationRepository: LocationRepository,
) : ViewModel() {

    val events: StateFlow<List<Event>> =
        eventsRepository.observeEvents()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _uiState = MutableStateFlow(EventsUiState())
    val uiState: StateFlow<EventsUiState> = _uiState.asStateFlow()

    init {
        refreshIfStale()
    }

    fun refreshIfStale() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true, errorMessage = null)
            when (val result = eventsRepository.refreshEventsIfStale()) {
                is RefreshResult.Success -> _uiState.value = _uiState.value.copy(isRefreshing = false)
                is RefreshResult.Failed -> _uiState.value =
                    _uiState.value.copy(isRefreshing = false, errorMessage = result.reason)
            }
        }
    }

    fun forceRefresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true, errorMessage = null)
            when (val result = eventsRepository.forceRefresh()) {
                is RefreshResult.Success -> _uiState.value = _uiState.value.copy(isRefreshing = false)
                is RefreshResult.Failed -> _uiState.value =
                    _uiState.value.copy(isRefreshing = false, errorMessage = result.reason)
            }
        }
    }

    fun setBookmarked(eventId: String, bookmarked: Boolean) {
        viewModelScope.launch {
            eventsRepository.setBookmarked(eventId, bookmarked)
        }
    }

    fun onLocationPermissionGranted() {
        if (_uiState.value.userLocation != null) return
        viewModelScope.launch {
            val location: UserLocation? = locationRepository.getLastKnownLocation()
            _uiState.value = _uiState.value.copy(userLocation = location)
        }
    }
}

data class EventsUiState(
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val userLocation: UserLocation? = null,
)

