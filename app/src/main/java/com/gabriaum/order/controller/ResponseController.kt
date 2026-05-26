package com.gabriaum.order.domain.controller

import com.gabriaum.order.domain.model.Response

class ResponseController: ArrayList<Response>() {
    init {
        add(Response("Question 1", "yes"))
        add(Response("Question 2", "yes"))
        add(Response("Question 3", "yes"))
        add(Response("Question 4", "yes"))
        add(Response("Question 5", "yes"))
        add(Response("Question 6 ", "yes"))
    }
}