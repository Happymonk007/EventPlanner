package com.example.eventplanner.ui.bookmarks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eventplanner.ui.components.EventCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(
    contentPadding: PaddingValues,
    onOpenDetails: (String) -> Unit,
    viewModel: BookmarksViewModel = hiltViewModel(),
) {
    val events by viewModel.bookmarkedEvents.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Bookmarks") })

        if (events.isEmpty()) {
            Text(
                text = "No bookmarks yet.",
                modifier = Modifier.padding(contentPadding).padding(16.dp),
                style = MaterialTheme.typography.bodyMedium,
            )
            return
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = contentPadding.calculateBottomPadding() + 16.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(events, key = { it.id }) { event ->
                EventCard(
                    event = event,
                    userLat = null,
                    userLng = null,
                    onToggleBookmark = { bookmarked -> viewModel.setBookmarked(event.id, bookmarked) },
                    onClick = { onOpenDetails(event.id) },
                )
            }
        }
    }
}

