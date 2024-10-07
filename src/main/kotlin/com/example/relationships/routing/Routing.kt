package com.example.relationships.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRelationshipRoutes() {
    routing {
        route("/api/v1/relationships") {
            get("/hello") {
                call.respond("hello")
            }
            post {
                // TODO: Implement post relationship
            }
            delete("/{id}") {
                // TODO: Implement delete relationship
            }
        }
    }
}