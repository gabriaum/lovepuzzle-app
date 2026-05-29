package com.gabriaum.app.backend.data.impl

import com.gabriaum.app.backend.data.ExpireData
import com.gabriaum.app.backend.database.DatabaseProvider

class ExpireDataImpl(private val db: DatabaseProvider) : ExpireData {
    override fun register() {
        db.executeInsert(
            "INSERT INTO userExpires(progressedLevels, availableAt) VALUES(?, ?);",
            arrayOf(0, -1)
        )
    }

    override fun blockForOneDay() {
        val oneDayMillis = 24 * 60 * 60 * 1000L
        val blockUntil = System.currentTimeMillis() + oneDayMillis
        db.executeUpdate(
            "UPDATE userExpires SET availableAt = ?;",
            arrayOf(blockUntil)
        )
    }

    override fun addLevelProgressed() {
        db.executeUpdate("UPDATE userExpires SET progressedLevels = progressedLevels + 1;")
    }

    override fun resetLevelProgressed() {
        db.executeUpdate("UPDATE userExpires SET progressedLevels = 0;")
    }

    override fun getProgressedLevels(): Int {
        return db.executeQuerySingle(
            "SELECT progressedLevels FROM userExpires;",
            emptyArray()
        ) { row -> (row["progressedLevels"] as? Int) ?: 0 } ?: 0
    }

    override fun getTime(): Long {
        val availableAt = db.executeQuerySingle(
            "SELECT availableAt FROM userExpires;",
            emptyArray()
        ) { row -> (row["availableAt"] as? Long) ?: -1L } ?: -1L

        if (availableAt <= 0) return 0L

        val currentTime = System.currentTimeMillis()
        val remainingTime = availableAt - currentTime
        return if (remainingTime > 0) remainingTime else 0L
    }

    override fun availableToContinue(limit: Int): Boolean {
        val availableAt = db.executeQuerySingle(
            "SELECT * FROM userExpires;",
            emptyArray()
        ) { row -> (row["availableAt"] as? Long) ?: -1L } ?: -1L

        return System.currentTimeMillis() >= availableAt
    }

    override fun exists(): Boolean {
        val result = db.executeQuerySingle(
            "SELECT 1 FROM userExpires;",
            emptyArray()
        ) { _ -> true }
        return result != null
    }
}