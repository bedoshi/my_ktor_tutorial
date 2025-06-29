package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.userRoutes
import com.example.sampleRoutes

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        // ユーザー関連のAPIルートを追加
        userRoutes()
        
        // サンプルAPIルートを追加
        sampleRoutes()
    }
}