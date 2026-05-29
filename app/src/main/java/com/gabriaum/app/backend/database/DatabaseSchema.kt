package com.gabriaum.app.backend.database

import android.database.sqlite.SQLiteDatabase

object DatabaseSchema {
    fun initializeTables(db: SQLiteDatabase) {
        createUserTable(db)
        createUserExpiresTable(db)
    }

    private fun createUserTable(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS user(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "level INTEGER)"
        )
    }

    private fun createUserExpiresTable(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS userExpires(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "progressedLevels INTEGER, " +
                    "availableAt BIGINT)"
        )
    }
}


