package com.example.routes

import com.example.models.users_models.User
import com.example.services.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.mindrot.jbcrypt.BCrypt

fun Application.configureAuthRoutes(){
    val userImpl = UserService()
    routing {
        post ("/v1/auth/login"){
            try {
                val payload = call.receive<User>()

                val user: User? = userImpl.getUser(payload.username)
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, "User not found!")
                }

                val success: Boolean = BCrypt.checkpw(payload.password, user?.password)
                if(!success){
                    call.respond(HttpStatusCode.BadRequest, "incorrect credentials!")
                }

                // todo: generate jwt for authenticate routes

                call.respond(HttpStatusCode.OK, "You're Logged in!")

            }catch (e: Exception){
                call.respond(HttpStatusCode.InternalServerError, "Error: ${e.message}")
            }
        }
    }
}