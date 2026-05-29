package com.gabriaum.app.backend.data

interface AccountData {
    fun register(level: Int)
    fun getLevel(): Int
    fun upLevel()
    fun exists(): Boolean
}