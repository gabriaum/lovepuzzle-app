package com.gabriaum.order.backend.database.sql

import android.content.Context
import android.database.sqlite.SQLiteDatabase

class SQLConnection(private val context: Context) {
    private var db: SQLiteDatabase? = null

    fun connect() {
        db = context.openOrCreateDatabase("gabriaum_order.db", Context.MODE_PRIVATE, null)
    }

    fun disconnect() {
        db?.close()
        db = null
    }

    fun getDatabase(): SQLiteDatabase {
        return db ?: throw IllegalStateException("Database not connected")
    }
}
