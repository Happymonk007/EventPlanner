package com.example.eventplanner.ui.events

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
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

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED,
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasLocationPermission = granted
            if (granted) viewModel.onLocationPermissionGranted()
        },
    )

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        val granted =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
        hasLocationPermission = granted
        if (granted) viewModel.onLocationPermissionGranted()
    }

    val requestLocationPermission: () -> Unit = {
        permissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    Column(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
        TopAppBar(
            title = { Text("Nearby Events") },
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f, fill = true),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (!hasLocationPermission) {
                item(key = "location_banner") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .clickable(
                                onClickLabel = "Enable location to see distance",
                                role = Role.Button,
                                onClick = requestLocationPermission,
                            ),
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
                    }
                }
            }

            uiState.errorMessage?.let { msg ->
                item(key = "error") {
                    Text(
                        text = msg,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }

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
