package com.example.eventplanner.ui.details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.eventplanner.ui.util.formatEpochMillis

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen(
    contentPadding: PaddingValues,
    eventId: String,
    onBack: () -> Unit,
    viewModel: EventDetailsViewModel = hiltViewModel(),
) {
    val eventFlow = viewModel.event(eventId)
    val event by eventFlow.collectAsState()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(event?.title ?: "Event details") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                val current = event
                if (current != null) {
                    IconButton(onClick = { viewModel.setBookmarked(current.id, !current.isBookmarked) }) {
                        if (current.isBookmarked) {
                            Icon(Icons.Filled.Bookmark, contentDescription = "Remove bookmark")
                        } else {
                            Icon(Icons.Filled.BookmarkBorder, contentDescription = "Add bookmark")
                        }
                    }
                }
            },
        )

        val current = event
        if (current == null) {
            Text(
                text = "Loading…",
                modifier = Modifier.padding(contentPadding).padding(16.dp),
            )
            return
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (current.imageUrl != null) {
                AsyncImage(
                    model = current.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(220.dp),
                )
            }

            Text(current.title, style = MaterialTheme.typography.headlineSmall)
            Text(
                "${current.locationName} • ${formatEpochMillis(current.startTimeEpochMillis)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {
                        val uri = Uri.parse("geo:${current.latitude},${current.longitude}?q=${current.latitude},${current.longitude}(${Uri.encode(current.title)})")
                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    },
                ) {
                    Icon(Icons.Filled.Map, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Open in Maps")
                }
            }
        }
    }
}

