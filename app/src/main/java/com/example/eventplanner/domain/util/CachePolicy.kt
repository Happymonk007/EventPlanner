package com.example.eventplanner.domain.util

object CachePolicy {
    fun isStale(
        lastFetchedAtEpochMillis: Long?,
        nowEpochMillis: Long,
        ttlMillis: Long,
    ): Boolean {
        if (lastFetchedAtEpochMillis == null) return true
        if (ttlMillis <= 0) return true
        return (nowEpochMillis - lastFetchedAtEpochMillis) > ttlMillis
    }
}

