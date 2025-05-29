package com.example

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class SampleRequest(
    val data: String
)

@Serializable
data class SampleResponse(
    val status: Int,
    val message: String
)

class SampleController {
    
    suspend fun handleSampleRequest(call: ApplicationCall) {
        try {
            val request = call.receive<SampleRequest>()
            
            val response = SampleResponse(
                status = 200,
                message = "success to get request"
            )
            
            call.respond(HttpStatusCode.OK, response)
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                SampleResponse(
                    status = 400,
                    message = "Invalid request format"
                )
            )
        }
    }
}

fun Route.sampleRoutes() {
    val controller = SampleController()
    
    route("/api") {
        post("/sample") {
            controller.handleSampleRequest(call)
        }
    }
}