package com.example.test

import com.example.SampleRequest
import com.example.SampleResponse
import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SampleControllerTest {

    @Test
    fun testSampleEndpointSuccess() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        val response = client.post("/api/sample") {
            contentType(ContentType.Application.Json)
            setBody("""{"data": "test data"}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val responseBody = Json.decodeFromString<SampleResponse>(response.bodyAsText())
        assertEquals(200, responseBody.status)
        assertEquals("success to get request", responseBody.message)
    }

    @Test
    fun testSampleEndpointWithComplexData() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        val response = client.post("/api/sample") {
            contentType(ContentType.Application.Json)
            setBody("""{"data": "Complex data with special characters: @#$%^&*()"}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val responseBody = Json.decodeFromString<SampleResponse>(response.bodyAsText())
        assertEquals(200, responseBody.status)
        assertEquals("success to get request", responseBody.message)
    }

    @Test
    fun testSampleEndpointWithEmptyData() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        val response = client.post("/api/sample") {
            contentType(ContentType.Application.Json)
            setBody("""{"data": ""}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val responseBody = Json.decodeFromString<SampleResponse>(response.bodyAsText())
        assertEquals(200, responseBody.status)
        assertEquals("success to get request", responseBody.message)
    }

    @Test
    fun testSampleEndpointInvalidJson() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        val response = client.post("/api/sample") {
            contentType(ContentType.Application.Json)
            setBody("""{"invalid": "json structure"}""")
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)

        val responseBody = Json.decodeFromString<SampleResponse>(response.bodyAsText())
        assertEquals(400, responseBody.status)
        assertEquals("Invalid request format", responseBody.message)
    }

    @Test
    fun testSampleEndpointMalformedJson() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        val response = client.post("/api/sample") {
            contentType(ContentType.Application.Json)
            setBody("""{"data": malformed json}""")
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)

        val responseBody = Json.decodeFromString<SampleResponse>(response.bodyAsText())
        assertEquals(400, responseBody.status)
        assertEquals("Invalid request format", responseBody.message)
    }

    @Test
    fun testSampleEndpointEmptyBody() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        val response = client.post("/api/sample") {
            contentType(ContentType.Application.Json)
            setBody("")
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)

        val responseBody = Json.decodeFromString<SampleResponse>(response.bodyAsText())
        assertEquals(400, responseBody.status)
        assertEquals("Invalid request format", responseBody.message)
    }

    @Test
    fun testSampleEndpointNoContentType() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        val response = client.post("/api/sample") {
            setBody("""{"data": "test"}""")
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun testSampleEndpointWithJapaneseCharacters() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        val response = client.post("/api/sample") {
            contentType(ContentType.Application.Json)
            setBody("""{"data": "テストデータ"}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val responseBody = Json.decodeFromString<SampleResponse>(response.bodyAsText())
        assertEquals(200, responseBody.status)
        assertEquals("success to get request", responseBody.message)
    }

    @Test
    fun testSampleEndpointWithLongData() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        val longData = "a".repeat(1000)
        val response = client.post("/api/sample") {
            contentType(ContentType.Application.Json)
            setBody("""{"data": "$longData"}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val responseBody = Json.decodeFromString<SampleResponse>(response.bodyAsText())
        assertEquals(200, responseBody.status)
        assertEquals("success to get request", responseBody.message)
    }
}