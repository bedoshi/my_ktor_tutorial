package com.example.test

import com.example.models.User
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import kotlinx.serialization.json.Json
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
        }

        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }

    @Test
    fun testUserEndpointsFlow() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        // Clear users before test
        com.example.clearUsersForTest()

        // 1. Initially, no user
        client.get("/users").apply {
            assertEquals(HttpStatusCode.OK, status)
            val response = bodyAsText()
            val users = Json.decodeFromString<List<User>>(response)
            assertTrue(users.isEmpty())
        }

        // 2. create a user
        val createResponse = client.post("/users") {
            contentType(ContentType.Application.Json)
            setBody("""{"name": "Flow Test", "email": "flow@example.com", "age": 50}""")
        }
        assertEquals(HttpStatusCode.Created, createResponse.status)

        // Extract user ID from response
        val createdUser = Json.decodeFromString<User>(createResponse.bodyAsText())
        val userId = createdUser.id

        // IDがnullでないことを確認
        assertNotNull(userId, "Created user ID should not be null")

        // 3. Get the user by ID
        client.get("/users/$userId").apply {
            assertEquals(HttpStatusCode.OK, status)
            val responseText = bodyAsText()
            assert(responseText.contains("Flow Test"))
            assert(responseText.contains("flow@example.com"))
        }

        // 4. Update the user
        client.put("/users/$userId") {
            contentType(ContentType.Application.Json)
            setBody("""{"name": "Updated Flow", "email": "updated-flow@example.com", "age": 51}""")
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }

        // 5. Verify update
        client.get("/users/$userId").apply {
            assertEquals(HttpStatusCode.OK, status)
            val responseText = bodyAsText()
            assert(responseText.contains("Updated Flow"))
            assert(responseText.contains("updated-flow@example.com"))
        }

        // 6. Delete the user
        client.delete("/users/$userId").apply {
            assertEquals(HttpStatusCode.NoContent, status)
        }

        // 7. Verify deletion
        client.get("/users/$userId").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }
}
