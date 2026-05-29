package com.gabriaum.app.domain.service

interface DailyLimitListener {
    fun onLimitActive(remainingTimeMs: Long)
    fun onTimerTick(hours: Long, minutes: Long, seconds: Long)
    fun onLimitExpired()
    fun onAvailable()
}

