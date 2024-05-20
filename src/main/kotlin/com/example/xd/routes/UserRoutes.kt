package com.example.xd.routes

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
        post("/v1/users") {
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
        get("/v1/users/{id}"){
            try {
                if(call.parameters["id"] == null){
                    call.respond(HttpStatusCode.BadRequest, "User id is required!")
                }

                val id: UUID? = call.parameters["id"]?.let { it1 -> UUID.fromString(it1) }
                val user: User? = userImpl.getUser(id!!)

                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                }

                call.respond(user!!)
            }catch (e: Exception){
                call.respond(HttpStatusCode.InternalServerError, "Error: ${e.message}")
            }
        }

    }
}
