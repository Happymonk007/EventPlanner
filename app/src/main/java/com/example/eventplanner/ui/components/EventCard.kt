package com.example.eventplanner.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.eventplanner.domain.model.Event
import com.example.eventplanner.ui.util.distanceMeters
import com.example.eventplanner.ui.util.formatDistance
import com.example.eventplanner.ui.util.formatEpochMillis
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.eventplanner.R
import com.example.eventplanner.ui.theme.Dimens

@Composable
fun EventCard(
    event: Event,
    userLat: Double?,
    userLng: Double?,
    onToggleBookmark: (Boolean) -> Unit,
    onClick: () -> Unit,
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (event.imageUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(event.imageUrl)
                        .size(800, 500)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(Dimens.imageCardHeight),
                    contentScale = ContentScale.Crop,
                )
            }
            Column(modifier = Modifier.padding(Dimens.cardContentPadding)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = event.title,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Spacer(modifier = Modifier.height(Dimens.spacerSm))
                        Text(
                            text = event.locationName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(Dimens.spacerXs))
                        Text(
                            text = formatEpochMillis(event.startTimeEpochMillis),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )

                        if (userLat != null && userLng != null) {
                            val meters = distanceMeters(userLat, userLng, event.latitude, event.longitude)
                            Spacer(modifier = Modifier.height(Dimens.spacerXs))
                            Text(
                                text = stringResource(R.string.distance_label, formatDistance(meters)),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }

                    IconButton(onClick = { onToggleBookmark(!event.isBookmarked) }) {
                        if (event.isBookmarked) {
                            Icon(Icons.Filled.Bookmark, contentDescription = stringResource(R.string.remove_bookmark))
                        } else {
                            Icon(Icons.Filled.BookmarkBorder, contentDescription = stringResource(R.string.add_bookmark))
                        }
                    }
                }
            }
        }
    }
}

