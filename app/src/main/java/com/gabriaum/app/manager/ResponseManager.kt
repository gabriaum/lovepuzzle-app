package com.gabriaum.app.manager

import com.gabriaum.app.model.Response

class ResponseManager {
    private val responses: MutableList<Response> = mutableListOf(
        Response("Question 1", "yes"),
        Response("Question 2", "yes"),
        Response("Question 3", "yes"),
        Response("Question 4", "yes"),
        Response("Question 5", "yes"),
        Response("Question 6 ", "yes")
    )

    fun getResponseCount(): Int = responses.size

    fun getResponse(index: Int): Response {
        if (index < 0 || index >= responses.size) {
            throw IndexOutOfBoundsException("Invalid response index: $index")
        }
        return responses[index]
    }

    operator fun get(index: Int): Response = getResponse(index)

    val size: Int
        get() = responses.size
}