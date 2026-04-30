package com.example.eventplanner.data.api

import retrofit2.http.GET
import retrofit2.http.Url

interface EventsApi {
    @GET
    suspend fun getEvents(@Url pathOrUrl: String): List<EventDto>
}

