package com.gabriaum.app.service

interface DailyLimitListener {
    fun onLimitActive(remainingTimeMs: Long)
    fun onTimerTick(hours: Long, minutes: Long, seconds: Long)
    fun onLimitExpired()
    fun onAvailable()
}

