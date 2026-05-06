package com.example.eventplanner.data.mappers

import com.example.eventplanner.data.api.EventDto
import com.example.eventplanner.data.db.EventEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class EventMappersTest {

    @Test
    fun `dto toEntity maps fields and sets fetchedAt`() {
        val dto = EventDto(
            id = "id1",
            title = "Title",
            locationName = "Loc",
            latitude = 1.23,
            longitude = 4.56,
            startTimeEpochMillis = 999L,
            imageUrl = "https://example.com/a.png",
        )

        val entity: EventEntity = dto.toEntity(
            fetchedAtEpochMillis = 1234L,
            isBookmarked = false,
        )
        assertEquals("id1", entity.id)
        assertEquals("Title", entity.title)
        assertEquals("Loc", entity.locationName)
        assertEquals(1.23, entity.latitude, 0.0)
        assertEquals(4.56, entity.longitude, 0.0)
        assertEquals(999L, entity.startTimeEpochMillis)
        assertEquals("https://example.com/a.png", entity.imageUrl)
        assertEquals(1234L, entity.fetchedAtEpochMillis)
    }

    @Test
    fun `eventEntity toDomain maps isBookmarked`() {
        val entity = EventEntity(
            id = "id1",
            title = "Title",
            locationName = "Loc",
            latitude = 1.0,
            longitude = 2.0,
            startTimeEpochMillis = 3L,
            imageUrl = null,
            fetchedAtEpochMillis = 4L,
            isBookmarked = true,
        )
        val domain = entity.toDomain()
        assertEquals("id1", domain.id)
        assertEquals(true, domain.isBookmarked)
    }
}

