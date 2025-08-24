package com.gabriaum.order.backend.data.impl

import android.database.sqlite.SQLiteDatabase
import com.gabriaum.order.backend.data.AccountData

class AccountDataImpl(db: SQLiteDatabase) : AccountData(db) {

    override fun register(name: String, level: Int) {
        db.execSQL("INSERT INTO users(name, level) VALUES(?, ?)", arrayOf(name, level))
    }

    override fun getLevel(name: String): Int {
        val cursor = db.rawQuery("SELECT level FROM users WHERE name = ?", arrayOf(name))
        val level = if (cursor.moveToFirst()) cursor.getInt(0) else 0
        cursor.close()
        return level
    }

    override fun upLevel(name: String) {
        val current = getLevel(name)
        db.execSQL("UPDATE users SET level = ? WHERE name = ?", arrayOf(current + 1, name))
    }

    override fun exists(name: String): Boolean {
        val cursor = db.rawQuery("SELECT 1 FROM users WHERE name = ?", arrayOf(name))
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }
}