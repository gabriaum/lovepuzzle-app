package com.gabriaum.order.backend.data.impl

import android.database.sqlite.SQLiteDatabase
import com.gabriaum.order.backend.data.ExpireData

class ExpireDataImpl(db: SQLiteDatabase) : ExpireData(db) {
    override fun register(name: String) {
        db.execSQL("INSERT INTO usersExpires(name, progressedLevels, availableAt) VALUES(?, ?, ?)", arrayOf(name, 0, -1))
    }

    override fun blockForOneDay(name: String) {
        val oneDayMillis = 24 * 60 * 60 * 1000L
        val blockUntil = System.currentTimeMillis() + oneDayMillis
        db.execSQL(
            "UPDATE usersExpires SET availableAt = ? WHERE name = ?",
            arrayOf(blockUntil, name)
        )
    }

    override fun addLevelProgressed(name: String) {
        db.execSQL(
            "UPDATE usersExpires SET progressedLevels = progressedLevels + 1 WHERE name = ?",
            arrayOf(name)
        )
    }

    override fun resetLevelProgressed(name: String) {
        db.execSQL(
            "UPDATE usersExpires SET progressedLevels = 0 WHERE name = ?",
            arrayOf(name)
        )
    }

    override fun getProgressedLevels(name: String): Int {
        val cursor = db.rawQuery(
            "SELECT progressedLevels FROM usersExpires WHERE name = ?",
            arrayOf(name)
        )
        var levels = 0
        if (cursor.moveToFirst()) {
            val index = cursor.getColumnIndex("progressedLevels")
            if (index != -1) {
                levels = cursor.getInt(index)
            }
        }
        cursor.close()
        return levels
    }

    override fun getTime(name: String): Long {
        val cursor = db.rawQuery("SELECT availableAt FROM usersExpires WHERE name = ?", arrayOf(name))
        var availableAt = -1L
        if (cursor.moveToFirst()) {
            val availableAtIndex = cursor.getColumnIndex("availableAt")
            if (availableAtIndex != -1) {
                availableAt = cursor.getLong(availableAtIndex)
            }
        }

        cursor.close()
        if (availableAt <= 0)
            return 0L

        val currentTime = System.currentTimeMillis()
        val remainingTime = availableAt - currentTime
        return if (remainingTime > 0) remainingTime else 0L
    }

    override fun availableToContinue(name: String, limit: Int): Boolean {
        val cursor = db.rawQuery("SELECT * FROM usersExpires WHERE name = ?", arrayOf(name))
        var availableAt = -1L
        if (cursor.moveToFirst()) {
            val availableAtIndex = cursor.getColumnIndex("availableAt")
            if (availableAtIndex != -1)
                availableAt = cursor.getLong(availableAtIndex)
        }

        cursor.close()
        return System.currentTimeMillis() >= availableAt
    }

    override fun exists(name: String): Boolean {
        val cursor = db.rawQuery("SELECT 1 FROM usersExpires WHERE name = ?", arrayOf(name))
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }
}