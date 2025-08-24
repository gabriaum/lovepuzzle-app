package com.gabriaum.order.backend.data

import android.database.sqlite.SQLiteDatabase

abstract class AccountData(internal val db: SQLiteDatabase) {

    init {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS users(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT UNIQUE, " +
                    "level INTEGER)"
        )
    }

    abstract fun register(name: String, level: Int)
    abstract fun getLevel(name: String): Int
    abstract fun upLevel(name: String)
    abstract fun exists(name: String): Boolean
}