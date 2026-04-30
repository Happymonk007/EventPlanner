package com.example.eventplanner.ui.events

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eventplanner.ui.components.EventCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    contentPadding: PaddingValues,
    onOpenDetails: (String) -> Unit,
    viewModel: EventsViewModel = hiltViewModel(),
) {
    val events by viewModel.events.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val hasLocationPermission =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) viewModel.onLocationPermissionGranted()
        },
    )

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) viewModel.onLocationPermissionGranted()
    }

    Column(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
        TopAppBar(
            title = { Text("Nearby Events") },
            actions = {
                if (uiState.isRefreshing) {
                    CircularProgressIndicator(modifier = Modifier.padding(end = 16.dp).height(18.dp))
                } else {
                    IconButton(onClick = { viewModel.forceRefresh() }) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
                    }
                }
            },
        )

        if (!hasLocationPermission) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(Icons.Filled.LocationOn, contentDescription = null)
                Column(modifier = Modifier.weight(1f)) {
                    Text("Enable location to see distance", style = MaterialTheme.typography.titleSmall)
                    Text(
                        "We use coarse location and only to calculate distance.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                IconButton(onClick = { permissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION) }) {
                    Icon(Icons.Filled.LocationOn, contentDescription = "Grant location permission")
                }
            }
        }

        uiState.errorMessage?.let { msg ->
            Text(
                text = msg,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                color = MaterialTheme.colorScheme.error,
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(events, key = { it.id }) { event ->
                EventCard(
                    event = event,
                    userLat = uiState.userLocation?.latitude,
                    userLng = uiState.userLocation?.longitude,
                    onToggleBookmark = { bookmarked -> viewModel.setBookmarked(event.id, bookmarked) },
                    onClick = { onOpenDetails(event.id) },
                )
            }
        }
    }
}
