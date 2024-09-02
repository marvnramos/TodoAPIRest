package com.example.tasks.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable
import java.util.*

fun Application.configureTaskRoutes() {
    routing {
        get("/hello") {
            call.respond(HttpStatusCode.OK, "hello")
        }

    }
}