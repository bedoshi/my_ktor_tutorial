package com.example

import com.example.models.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class UserController {
    // インメモリストレージ（実際のアプリケーションではデータベースを使用する）
    private val users = mutableListOf<User>()
    private var userIdCounter = 1

    // テスト用にユーザーリストをクリアする関数
    fun clearUsersForTest() {
        users.clear()
        userIdCounter = 1
    }

    // 全ユーザーの取得
    suspend fun getAllUsers(call: ApplicationCall) {
        if (users.isEmpty()) {
            call.respond(HttpStatusCode.OK, emptyList<User>())
        } else {
            call.respond(HttpStatusCode.OK, users)
        }
    }

    // IDでユーザーを取得
    suspend fun getUserById(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
            return
        }

        val user = users.find { it.id == id }
        if (user == null) {
            call.respond(HttpStatusCode.NotFound, "User not found")
        } else {
            call.respond(HttpStatusCode.OK, user)
        }
    }

    // 新規ユーザーの作成
    suspend fun createUser(call: ApplicationCall) {
        val user = call.receive<User>()
        val newUser = user.copy(id = userIdCounter++)
        users.add(newUser)
        call.respond(HttpStatusCode.Created, newUser)
    }

    // ユーザーの更新
    suspend fun updateUser(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
            return
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
    suspend fun deleteUser(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
            return
        }

        val removed = users.removeIf { it.id == id }
        if (removed) {
            call.respond(HttpStatusCode.NoContent)
        } else {
            call.respond(HttpStatusCode.NotFound, "User not found")
        }
    }
}

// シングルトンインスタンス（テスト用にclearUsersForTestを呼び出せるようにする）
val userController = UserController()

// テスト用にユーザーリストをクリアする関数
fun clearUsersForTest() {
    userController.clearUsersForTest()
}

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