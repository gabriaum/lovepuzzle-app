package com.gabriaum.app.backend.database.sql

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.gabriaum.app.backend.database.DatabaseSchema
import com.gabriaum.app.backend.database.DatabaseProvider

class SQLConnection(private val context: Context) : DatabaseProvider {
    private var db: SQLiteDatabase? = null

    fun connect() {
        db = context.openOrCreateDatabase("lovepuzzle.db", Context.MODE_PRIVATE, null)
        db?.let { DatabaseSchema.initializeTables(it) }
    }

    fun disconnect() {
        db?.close()
        db = null
    }

    private fun getDatabase(): SQLiteDatabase {
        return db ?: throw IllegalStateException("Database not connected")
    }

    override fun executeInsert(sql: String, args: Array<Any?>) {
        getDatabase().execSQL(sql, args)
    }

    override fun executeUpdate(sql: String, args: Array<Any?>) {
        getDatabase().execSQL(sql, args)
    }

    override fun executeDelete(sql: String, args: Array<Any?>) {
        getDatabase().execSQL(sql, args)
    }

    override fun <T> executeQuery(
        sql: String,
        args: Array<String>,
        mapper: (Map<String, Any>) -> T
    ): List<T> {
        val cursor = getDatabase().rawQuery(sql, args)
        val results = mutableListOf<T>()

        if (cursor.moveToFirst()) {
            do {
                val rowData = mutableMapOf<String, Any>()
                for (i in 0 until cursor.columnCount) {
                    val columnName = cursor.getColumnName(i)
                    val value: Any? = when (cursor.getType(i)) {
                        1 -> cursor.getInt(i)
                        2 -> cursor.getFloat(i)
                        3 -> cursor.getString(i)
                        4 -> cursor.getBlob(i)
                        else -> null
                    }
                    if (value != null) {
                        rowData[columnName] = value
                    }
                }
                results.add(mapper(rowData))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return results
    }

    override fun <T> executeQuerySingle(
        sql: String,
        args: Array<String>,
        mapper: (Map<String, Any>) -> T?
    ): T? {
        val results = executeQuery(sql, args, mapper)
        return if (results.isNotEmpty()) results[0] else null
    }
}