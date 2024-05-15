package com.example.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.services.UserService
import com.example.models.users_models.User
import io.ktor.http.*
import io.ktor.server.request.*
import org.mindrot.jbcrypt.BCrypt
import java.util.UUID

fun Application.configureUserRoutes() {
    val userImpl = UserService()
    routing {
        post("/") {
            try {
                val requestBody: User = call.receive<User>()

                val hashedPassword = BCrypt.hashpw(requestBody.password, BCrypt.gensalt())
                requestBody.password = hashedPassword

                val user = userImpl.addUser(requestBody)
                call.respond(user ?: HttpStatusCode.InternalServerError)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error processing request")
            }
        }
    }
}
