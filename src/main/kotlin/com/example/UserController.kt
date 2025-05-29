package com.example

import com.example.models.User
import com.example.services.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class UserController(private val userService: UserService) {

    // 全ユーザーの取得
    suspend fun getAllUsers(call: ApplicationCall) {
        val users = userService.getAllUsers()
        call.respond(HttpStatusCode.OK, users)
    }

    // IDでユーザーを取得
    suspend fun getUserById(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
            return
        }

        val user = userService.getUserById(id)
        if (user == null) {
            call.respond(HttpStatusCode.NotFound, "User not found")
        } else {
            call.respond(HttpStatusCode.OK, user)
        }
    }

    // 新規ユーザーの作成
    suspend fun createUser(call: ApplicationCall) {
        try {
            val user = call.receive<User>()
            val newUser = userService.createUser(user)
            call.respond(HttpStatusCode.Created, newUser)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Failed to create user: ${e.message}")
        }
    }

    // ユーザーの更新
    suspend fun updateUser(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
            return
        }

        try {
            val user = call.receive<User>()
            val updated = userService.updateUser(id, user)
            if (updated) {
                val updatedUser = user.copy(id = id)
                call.respond(HttpStatusCode.OK, updatedUser)
            } else {
                call.respond(HttpStatusCode.NotFound, "User not found")
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Failed to update user: ${e.message}")
        }
    }

    // ユーザーの削除
    suspend fun deleteUser(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
            return
        }

        val deleted = userService.deleteUser(id)
        if (deleted) {
            call.respond(HttpStatusCode.NoContent)
        } else {
            call.respond(HttpStatusCode.NotFound, "User not found")
        }
    }
}

// シングルトンインスタンス
val userController = UserController(UserService())

fun Route.userRoutes() {
    route("/users") {
        // 全ユーザーの取得
        get {
            userController.getAllUsers(call)
        }

        // IDでユーザーを取得
        get("/{id}") {
            userController.getUserById(call)
        }

        // 新規ユーザーの作成
        post {
            userController.createUser(call)
        }

        // ユーザーの更新
        put("/{id}") {
            userController.updateUser(call)
        }

        // ユーザーの削除
        delete("/{id}") {
            userController.deleteUser(call)
        }
    }
}