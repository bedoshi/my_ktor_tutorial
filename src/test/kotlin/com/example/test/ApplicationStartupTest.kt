package com.example.test

import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationStartupTest {

    @BeforeEach
    fun setup() {
        com.example.clearUsersForTest()
    }

    @Test
    fun testApplicationStartup() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        // Test that the application starts and basic routes are accessible
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }

    @Test
    fun testApplicationConfiguration() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        // Test that all main routes are configured
        // Test users route
        client.get("/users").apply {
            assertEquals(HttpStatusCode.OK, status)
        }

        // Test sample API route
        client.post("/api/sample") {
            contentType(ContentType.Application.Json)
            setBody("""{"data": "test"}""")
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun testMainFunctionCoverage() {
        // This test ensures the main function is covered
        // In a real application, you might want to test this differently
        // For coverage purposes, we're testing the configuration functions
        testApplication {
            application {
                // Simulate what main() does
                configureRouting()
                configureSerialization()
            }

            // Verify the server is configured correctly
            client.get("/").apply {
                assertEquals(HttpStatusCode.OK, status)
            }
        }
    }

    @Test
    fun testAllPluginsLoaded() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        // Test that serialization is working (by sending JSON)
        client.post("/users") {
            contentType(ContentType.Application.Json)
            setBody("""{"name": "Plugin Test", "email": "plugin@test.com", "age": 25}""")
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
            // Response should be valid JSON
            assertTrue(bodyAsText().contains("Plugin Test"))
        }
    }
}