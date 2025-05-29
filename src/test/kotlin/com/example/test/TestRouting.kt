package com.example.test

import com.example.sampleRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureTestRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        sampleRoutes()
        testUserRoutes()
    }
}