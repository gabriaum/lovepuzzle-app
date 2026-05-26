package com.gabriaum.order.backend.data

import android.database.sqlite.SQLiteDatabase

abstract class ExpireData(internal val db: SQLiteDatabase) {
    init {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS userExpires(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "progressedLevels INTEGER, " +
                    "availableAt BIGINT)"
        )
    }

    abstract fun register()
    abstract fun blockForOneDay()
    abstract fun addLevelProgressed()
    abstract fun resetLevelProgressed()
    abstract fun getProgressedLevels(): Int
    abstract fun getTime(): Long
    abstract fun availableToContinue(limit: Int): Boolean
    abstract fun exists(): Boolean
}