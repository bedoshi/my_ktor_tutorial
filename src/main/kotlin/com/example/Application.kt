package com.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSerialization()
        // configureAuthentication() // 必要に応じて認証を設定
        // configureDatabases() // 必要に応じてデータベースを設定
    }.start(wait = true)
}