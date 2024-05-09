package com.example.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.dao.implementations.DAOUserImpl
import com.example.models.users_models.User
import com.example.models.users_models.Users
import io.ktor.http.*
import io.ktor.server.request.*

fun Application.configureRouting() {
    val userImpl = DAOUserImpl()
    routing {
        post("/") {
//            val request: User = call.receive()
//            request?.let { user ->
//                val savedUser = userImpl.addUser(user)
//                call.respond(savedUser.toString())
//            }?:call.respond(HttpStatusCode.BadRequest, "Invalid user data")
            val requestBody: User = call.receive<User>()
            val user = userImpl.addUser(requestBody)
            call.respond(user.toString())
        }
    }
}
