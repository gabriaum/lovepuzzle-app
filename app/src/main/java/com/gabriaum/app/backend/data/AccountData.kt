package com.gabriaum.app.backend.data

import android.database.sqlite.SQLiteDatabase

abstract class AccountData(internal val db: SQLiteDatabase) {
    init {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS user(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "level INTEGER)"
        )
    }

    abstract fun register(level: Int)
    abstract fun getLevel(): Int
    abstract fun upLevel()
    abstract fun exists(): Boolean
}