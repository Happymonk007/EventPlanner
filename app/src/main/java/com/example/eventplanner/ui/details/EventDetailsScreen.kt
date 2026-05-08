package com.example.eventplanner.ui.details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.eventplanner.ui.util.formatEpochMillis
import androidx.core.net.toUri
import androidx.compose.ui.res.stringResource
import com.example.eventplanner.R
import com.example.eventplanner.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen(
    contentPadding: PaddingValues,
    onBack: () -> Unit,
    viewModel: EventDetailsViewModel = hiltViewModel(),
) {
    val event by viewModel.event.collectAsState()
    val context = LocalContext.current
    val scroll = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(event?.title ?: stringResource(R.string.title_event_details_fallback)) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                }
            },
            actions = {
                val current = event
                if (current != null) {
                    IconButton(onClick = { viewModel.setBookmarked(!current.isBookmarked) }) {
                        if (current.isBookmarked) {
                            Icon(Icons.Filled.Bookmark, contentDescription = stringResource(R.string.remove_bookmark))
                        } else {
                            Icon(Icons.Filled.BookmarkBorder, contentDescription = stringResource(R.string.add_bookmark))
                        }
                    }
                }
            },
        )

        val current = event
        if (current == null) {
            Text(
                text = stringResource(R.string.loading),
                modifier = Modifier.padding(contentPadding).padding(Dimens.screenPadding),
            )
            return
        }

        val imageUrl = current.imageUrl
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = contentPadding.calculateBottomPadding())
                .verticalScroll(scroll),
        ) {
            if (imageUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(imageUrl)
                        .size(800, 500)
                        .memoryCacheKey(imageUrl)
                        .diskCacheKey(imageUrl)
                        .crossfade(false)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.detailsImageHeight),
                    contentScale = ContentScale.Crop,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.screenPadding, vertical = Dimens.screenPadding),
                verticalArrangement = Arrangement.spacedBy(Dimens.listItemSpacing),
            ) {
                Text(current.title, style = MaterialTheme.typography.headlineSmall)
                Text(
                    stringResource(
                        R.string.event_details_subtitle,
                        current.locationName,
                        formatEpochMillis(current.startTimeEpochMillis),
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(modifier = Modifier.height(Dimens.spacerSm))

                val mapsBlue = Color(0xFF4285F4)
                Button(
                    onClick = {
                        val uri =
                            "geo:${current.latitude},${current.longitude}?q=${current.latitude},${current.longitude}(${
                                Uri.encode(current.title)
                            })".toUri()
                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = mapsBlue,
                        contentColor = Color.White,
                    ),
                ) {
                    Icon(
                        painter = painterResource(android.R.drawable.ic_dialog_map),
                        contentDescription = null,
                        tint = Color.White,
                    )
                    Spacer(modifier = Modifier.width(Dimens.spacerMd))
                    Text(stringResource(R.string.open_in_maps))
                }
            }
        }
    }
}
