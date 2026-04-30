package com.example.eventplanner.ui.util

import org.junit.Assert.assertEquals
import org.junit.Test

class FormattersTest {

    @Test
    fun `formatDistance formats meters under 1km`() {
        assertEquals("999 m", formatDistance(999f))
        assertEquals("0 m", formatDistance(0f))
    }

    @Test
    fun `formatDistance formats km at or above 1km`() {
        assertEquals("1.0 km", formatDistance(1000f))
        assertEquals("12.5 km", formatDistance(12_500f))
    }
}

