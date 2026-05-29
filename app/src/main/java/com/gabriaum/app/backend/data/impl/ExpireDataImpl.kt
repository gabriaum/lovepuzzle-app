package com.gabriaum.app.backend.data.impl

import android.database.sqlite.SQLiteDatabase
import com.gabriaum.app.backend.data.ExpireData

class ExpireDataImpl(private val db: SQLiteDatabase) : ExpireData {
    override fun register() {
        db.execSQL("INSERT INTO userExpires(progressedLevels, availableAt) VALUES(?, ?);", arrayOf(0, -1))
    }

    override fun blockForOneDay() {
        val oneDayMillis = 24 * 60 * 60 * 1000L
        val blockUntil = System.currentTimeMillis() + oneDayMillis
        db.execSQL(
            "UPDATE userExpires SET availableAt = ?;",
            arrayOf(blockUntil)
        )
    }

    override fun addLevelProgressed() {
        db.execSQL(
            "UPDATE userExpires SET progressedLevels = progressedLevels + 1;"
        )
    }

    override fun resetLevelProgressed() {
        db.execSQL(
            "UPDATE userExpires SET progressedLevels = 0;"
        )
    }

    override fun getProgressedLevels(): Int {
        val cursor = db.rawQuery(
            "SELECT progressedLevels FROM userExpires;",
            arrayOf()
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

    override fun getTime(): Long {
        val cursor = db.rawQuery("SELECT availableAt FROM userExpires;", arrayOf())
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

    override fun availableToContinue(limit: Int): Boolean {
        val cursor = db.rawQuery("SELECT * FROM userExpires;", arrayOf())
        var availableAt = -1L
        if (cursor.moveToFirst()) {
            val availableAtIndex = cursor.getColumnIndex("availableAt")
            if (availableAtIndex != -1)
                availableAt = cursor.getLong(availableAtIndex)
        }

        cursor.close()
        return System.currentTimeMillis() >= availableAt
    }

    override fun exists(): Boolean {
        val cursor = db.rawQuery("SELECT 1 FROM userExpires;", arrayOf())
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }
}