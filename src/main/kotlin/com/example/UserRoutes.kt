package com.example

import com.example.models.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// インメモリストレージ（実際のアプリケーションではデータベースを使用する）
private val users = mutableListOf<User>()
private var userIdCounter = 1

fun Route.userRoutes() {
    route("/users") {
        // 全ユーザーの取得
        get {
            if (users.isEmpty()) {
                call.respond(HttpStatusCode.OK, emptyList<User>())
            } else {
                call.respond(HttpStatusCode.OK, users)
            }
        }

        // IDでユーザーを取得
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
                return@get
            }

            val user = users.find { it.id == id }
            if (user == null) {
                call.respond(HttpStatusCode.NotFound, "User not found")
            } else {
                call.respond(HttpStatusCode.OK, user)
            }
        }

        // 新規ユーザーの作成
        post {
            val user = call.receive<User>()
            val newUser = user.copy(id = userIdCounter++)
            users.add(newUser)
            call.respond(HttpStatusCode.Created, newUser)
        }

        // ユーザーの更新
        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
                return@put
            }

            val userIndex = users.indexOfFirst { it.id == id }
            if (userIndex == -1) {
                call.respond(HttpStatusCode.NotFound, "User not found")
            } else {
                val updatedUser = call.receive<User>().copy(id = id)
                users[userIndex] = updatedUser
                call.respond(HttpStatusCode.OK, updatedUser)
            }
        }

        // ユーザーの削除
        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
                return@delete
            }

            val removed = users.removeIf { it.id == id }
            if (removed) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, "User not found")
            }
        }
    }
}