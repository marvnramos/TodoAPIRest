package com.example.relationships.routing

import io.ktor.http.*
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
                call.respond(HttpStatusCode.NotImplemented, "Make friend endpoint not implemented yet.")
            }
            delete("/{id}") {
                // TODO: Implement delete relationship
                call.respond(HttpStatusCode.NotImplemented, "Unfriend endpoint not implemented yet.")
            }
        }
    }
}