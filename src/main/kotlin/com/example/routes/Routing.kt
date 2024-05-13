package com.example.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.dao.implementations.DAOUserImpl
import com.example.models.users_models.User
import com.example.models.users_models.Users
import io.ktor.http.*
import io.ktor.server.request.*
import java.util.*
import kotlin.reflect.typeOf

fun Application.configureRouting() {
    val userImpl = DAOUserImpl()
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
