package com.gabriaum.order.backend.data

import android.database.sqlite.SQLiteDatabase

abstract class ExpireData(internal val db: SQLiteDatabase) {

    init {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS usersExpires(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT UNIQUE, " +
                    "progressedLevels INTEGER, " +
                    "availableAt BIGINT)"
        )
    }

    abstract fun register(name: String)
    abstract fun blockForOneDay(name: String)
    abstract fun addLevelProgressed(name: String)
    abstract fun resetLevelProgressed(name: String)
    abstract fun getProgressedLevels(name: String): Int
    abstract fun getTime(name: String): Long
    abstract fun availableToContinue(name: String, limit: Int): Boolean
    abstract fun exists(name: String): Boolean
}