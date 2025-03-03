package com.example.test

import com.example.models.User
import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UserRoutesTest {
    @Test
    fun testGetUsersEmpty() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        client.get("/users").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("[]", bodyAsText())
        }
    }

    @Test
    fun testCreateUser() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        // Create a new user
        val response = client.post("/users") {
            contentType(ContentType.Application.Json)
            setBody("""{"name": "Test User", "email": "test@example.com", "age": 25}""")
        }

        assertEquals(HttpStatusCode.Created, response.status)

        val responseText = response.bodyAsText()
        val createdUser = Json.decodeFromString<User>(responseText)

        assertEquals("Test User", createdUser.name)
        assertEquals("test@example.com", createdUser.email)
        assertEquals(25, createdUser.age)
        assertNotNull(createdUser.id)
    }

    @Test
    fun testGetUserById() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        // First create a user
        val createResponse = client.post("/users") {
            contentType(ContentType.Application.Json)
            setBody("""{"name": "Get Test", "email": "get@example.com", "age": 30}""")
        }

        val createdUser = Json.decodeFromString<User>(createResponse.bodyAsText())
        val userId = createdUser.id

        // Now get the user by ID
        client.get("/users/$userId").apply {
            assertEquals(HttpStatusCode.OK, status)

            val fetchedUser = Json.decodeFromString<User>(bodyAsText())
            assertEquals(userId, fetchedUser.id)
            assertEquals("Get Test", fetchedUser.name)
            assertEquals("get@example.com", fetchedUser.email)
            assertEquals(30, fetchedUser.age)
        }
    }

    @Test
    fun testGetUserNotFound() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        client.get("/users/999").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun testUpdateUser() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        // First create a user
        val createResponse = client.post("/users") {
            contentType(ContentType.Application.Json)
            setBody("""{"name": "Update Test", "email": "update@example.com", "age": 35}""")
        }

        val createdUser = Json.decodeFromString<User>(createResponse.bodyAsText())
        val userId = createdUser.id

        // Now update the user
        val updateResponse = client.put("/users/$userId") {
            contentType(ContentType.Application.Json)
            setBody("""{"name": "Updated Name", "email": "updated@example.com", "age": 36}""")
        }

        assertEquals(HttpStatusCode.OK, updateResponse.status)

        val updatedUser = Json.decodeFromString<User>(updateResponse.bodyAsText())
        assertEquals(userId, updatedUser.id)
        assertEquals("Updated Name", updatedUser.name)
        assertEquals("updated@example.com", updatedUser.email)
        assertEquals(36, updatedUser.age)

        // Verify the user was updated by fetching it again
        client.get("/users/$userId").apply {
            assertEquals(HttpStatusCode.OK, status)

            val fetchedUser = Json.decodeFromString<User>(bodyAsText())
            assertEquals("Updated Name", fetchedUser.name)
        }
    }

    @Test
    fun testDeleteUser() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        // First create a user
        val createResponse = client.post("/users") {
            contentType(ContentType.Application.Json)
            setBody("""{"name": "Delete Test", "email": "delete@example.com", "age": 40}""")
        }

        val createdUser = Json.decodeFromString<User>(createResponse.bodyAsText())
        val userId = createdUser.id

        // Now delete the user
        client.delete("/users/$userId").apply {
            assertEquals(HttpStatusCode.NoContent, status)
        }

        // Verify the user was deleted
        client.get("/users/$userId").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun testGetAllUsers() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        // Create a few users
        client.post("/users") {
            contentType(ContentType.Application.Json)
            setBody("""{"name": "User 1", "email": "user1@example.com", "age": 25}""")
        }

        client.post("/users") {
            contentType(ContentType.Application.Json)
            setBody("""{"name": "User 2", "email": "user2@example.com", "age": 30}""")
        }

        // Get all users
        val response = client.get("/users")
        assertEquals(HttpStatusCode.OK, response.status)

        val responseText = response.bodyAsText()
        assertTrue(responseText.contains("User 1"))
        assertTrue(responseText.contains("User 2"))
    }
}