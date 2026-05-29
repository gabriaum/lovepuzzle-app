package com.gabriaum.app.backend.data.impl

import android.database.sqlite.SQLiteDatabase
import com.gabriaum.app.backend.data.AccountData

class AccountDataImpl(db: SQLiteDatabase) : AccountData(db) {
    override fun register(level: Int) {
        db.execSQL("INSERT INTO user(level) VALUES(?)", arrayOf(level))
    }

    override fun getLevel(): Int {
        val cursor = db.rawQuery("SELECT level FROM user;", arrayOf())
        val level = if (cursor.moveToFirst()) cursor.getInt(0) else 0
        cursor.close()
        return level
    }

    override fun upLevel() {
        val current = getLevel()
        db.execSQL("UPDATE user SET level = ?;", arrayOf(current + 1))
    }

    override fun exists(): Boolean {
        val cursor = db.rawQuery("SELECT 1 FROM user;", arrayOf())
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }
}