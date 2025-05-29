package com.example.test

import com.example.UserController
import com.example.services.UserService
import io.ktor.server.routing.*
import io.ktor.server.application.*

// Create a test-specific UserController instance
val testUserController = UserController(UserService())

// Extension function to configure user routes with test controller
fun Route.testUserRoutes() {
    route("/users") {
        // 全ユーザーの取得
        get {
            testUserController.getAllUsers(call)
        }

        // IDでユーザーを取得
        get("/{id}") {
            testUserController.getUserById(call)
        }

        // 新規ユーザーの作成
        post {
            testUserController.createUser(call)
        }

        // ユーザーの更新
        put("/{id}") {
            testUserController.updateUser(call)
        }

        // ユーザーの削除
        delete("/{id}") {
            testUserController.deleteUser(call)
        }
    }
}