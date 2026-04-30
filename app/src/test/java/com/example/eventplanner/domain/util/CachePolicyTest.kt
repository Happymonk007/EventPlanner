package com.example.eventplanner.domain.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CachePolicyTest {

    @Test
    fun `isStale returns true when never fetched`() {
        assertTrue(CachePolicy.isStale(lastFetchedAtEpochMillis = null, nowEpochMillis = 1000L, ttlMillis = 100L))
    }

    @Test
    fun `isStale returns false when within ttl`() {
        assertFalse(CachePolicy.isStale(lastFetchedAtEpochMillis = 1000L, nowEpochMillis = 1050L, ttlMillis = 100L))
    }

    @Test
    fun `isStale returns true when beyond ttl`() {
        assertTrue(CachePolicy.isStale(lastFetchedAtEpochMillis = 1000L, nowEpochMillis = 1201L, ttlMillis = 200L))
    }
}

