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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.abs
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val eventsRepository: EventsRepository,
    private val locationRepository: LocationRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventsUiState())
    val uiState: StateFlow<EventsUiState> = _uiState.asStateFlow()

    val events: StateFlow<List<Event>> =
        eventsRepository.observeEvents()
            .combine(uiState.map { it.userLocation }.distinctUntilChanged()) { events, location ->
                when (location) {
                    null -> events.sortedBy { it.startTimeEpochMillis }
                    else -> events.sortedBy { e -> distanceMetersApprox(location, e) }
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(errorMessage = null)
            when (val result = eventsRepository.loadEvents()) {
                is RefreshResult.Success -> Unit
                is RefreshResult.Failed -> _uiState.value = _uiState.value.copy(errorMessage = result.reason)
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
    val errorMessage: String? = null,
    val userLocation: UserLocation? = null,
)

private fun distanceMetersApprox(location: UserLocation, event: Event): Double {
    val latDiff = event.latitude - location.latitude
    val lngDiff = event.longitude - location.longitude
    val metersPerDegreeLat = 111_000.0
    val metersPerDegreeLng = 111_000.0 * kotlin.math.cos(location.latitude * Math.PI / 180.0)
    val dx = latDiff * metersPerDegreeLat
    val dy = lngDiff * metersPerDegreeLng
    return abs(dx) + abs(dy)
}

