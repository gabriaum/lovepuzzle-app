package com.gabriaum.app.backend.data.impl

import com.gabriaum.app.backend.data.AccountData
import com.gabriaum.app.backend.database.DatabaseProvider

class AccountDataImpl(private val db: DatabaseProvider) : AccountData {
    override fun register(level: Int) {
        db.executeInsert("INSERT INTO user(level) VALUES(?)", arrayOf(level))
    }

    override fun getLevel(): Int {
        return db.executeQuerySingle(
            "SELECT level FROM user;",
            emptyArray()
        ) { row -> (row["level"] as? Int) ?: 0 } ?: 0
    }

    override fun upLevel() {
        val current = getLevel()
        db.executeUpdate("UPDATE user SET level = ?;", arrayOf(current + 1))
    }

    override fun exists(): Boolean {
        val result = db.executeQuerySingle(
            "SELECT 1 FROM user;",
            emptyArray()
        ) { _ -> true }
        return result != null
    }
}