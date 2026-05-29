package com.gabriaum.app.backend.data

interface ExpireData {
    fun register()
    fun blockForOneDay()
    fun addLevelProgressed()
    fun resetLevelProgressed()
    fun getProgressedLevels(): Int
    fun getTime(): Long
    fun availableToContinue(limit: Int): Boolean
    fun exists(): Boolean
}