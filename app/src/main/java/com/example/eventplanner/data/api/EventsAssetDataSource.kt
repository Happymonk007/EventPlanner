package com.example.eventplanner.data.api

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class EventsAssetDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val moshi: Moshi,
) {
    fun readEventsFromAssets(assetFileName: String = "events.json"): List<EventDto> {
        val json = context.assets.open(assetFileName).bufferedReader().use { it.readText() }
        val type = Types.newParameterizedType(List::class.java, EventDto::class.java)
        val adapter = moshi.adapter<List<EventDto>>(type)
        return adapter.fromJson(json).orEmpty()
    }
}