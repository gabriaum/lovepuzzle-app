package com.gabriaum.app.domain.controller

import com.gabriaum.app.domain.model.Response

class ResponseManager: ArrayList<Response>() {
    init {
        add(Response("Question 1", "yes"))
        add(Response("Question 2", "yes"))
        add(Response("Question 3", "yes"))
        add(Response("Question 4", "yes"))
        add(Response("Question 5", "yes"))
        add(Response("Question 6 ", "yes"))
    }
}