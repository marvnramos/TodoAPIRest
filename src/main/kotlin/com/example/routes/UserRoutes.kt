package com.example.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.services.UserService
import com.example.models.users_models.User
import io.ktor.http.*
import io.ktor.server.request.*

fun Application.configureUserRoutes() {
    val userImpl = UserService()
    routing {
        post("/") {
            try {
                val requestBody: User = call.receive<User>()
                val user = userImpl.addUser(requestBody)
                call.respond(user ?: HttpStatusCode.InternalServerError)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error processing request")
            }
        }
    }
}
