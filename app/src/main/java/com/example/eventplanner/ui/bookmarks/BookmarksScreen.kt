package com.example.eventplanner.ui.bookmarks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eventplanner.ui.components.EventCard
import androidx.compose.ui.res.stringResource
import com.example.eventplanner.R
import com.example.eventplanner.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(
    contentPadding: PaddingValues,
    onOpenDetails: (String) -> Unit,
    viewModel: BookmarksViewModel = hiltViewModel(),
) {
    val events by viewModel.bookmarkedEvents.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text(stringResource(R.string.title_bookmarks)) })

        if (events.isEmpty()) {
            Text(
                text = stringResource(R.string.bookmarks_empty),
                modifier = Modifier.padding(contentPadding).padding(Dimens.screenPadding),
                style = MaterialTheme.typography.bodyMedium,
            )
            return
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = Dimens.screenPadding,
                end = Dimens.screenPadding,
                top = Dimens.screenPadding,
                bottom = contentPadding.calculateBottomPadding() + Dimens.screenPadding,
            ),
            verticalArrangement = Arrangement.spacedBy(Dimens.listItemSpacing),
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

