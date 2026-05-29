package com.gabriaum.app.backend.database.sql

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.gabriaum.app.backend.database.DatabaseSchema

class SQLConnection(private val context: Context) {
    private var db: SQLiteDatabase? = null

    fun connect() {
        db = context.openOrCreateDatabase("lovepuzzle.db", Context.MODE_PRIVATE, null)
        db?.let { DatabaseSchema.initializeTables(it) }
    }

    fun disconnect() {
        db?.close()
        db = null
    }

    fun getDatabase(): SQLiteDatabase {
        return db ?: throw IllegalStateException("Database not connected")
    }
}
