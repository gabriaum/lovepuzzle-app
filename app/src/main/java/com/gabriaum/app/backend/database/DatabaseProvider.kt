package com.gabriaum.app.backend.database

interface DatabaseProvider {
    fun executeInsert(sql: String, args: Array<Any?>)
    fun executeUpdate(sql: String, args: Array<Any?> = emptyArray())
    fun executeDelete(sql: String, args: Array<Any?> = emptyArray())
    fun <T> executeQuery(sql: String, args: Array<String> = emptyArray(), mapper: (Map<String, Any>) -> T): List<T>
    fun <T> executeQuerySingle(sql: String, args: Array<String> = emptyArray(), mapper: (Map<String, Any>) -> T?): T?
}