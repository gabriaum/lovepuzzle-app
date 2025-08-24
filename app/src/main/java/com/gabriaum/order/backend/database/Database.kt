package com.gabriaum.order.backend.database

import android.database.sqlite.SQLiteDatabase

interface Database {
    fun connect()
    fun disconnect()
    fun getConnector(): SQLiteDatabase
}