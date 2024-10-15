package com.example.notifications.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureNotificationsRouting() {
    routing {
        route("/api/v1/noti-test") {
            get("/hello") {
                call.respond(HttpStatusCode.OK, "hellou")
            }
            post {
                // TODO: Implement post notification
                call.respond(HttpStatusCode.NotImplemented, "Post notification endpoint not implemented yet.")
            }
            patch("/mark-as-read/{id}") {
                // TODO: Implement mark as read
                call.respond(HttpStatusCode.NotImplemented, "Mark as read endpoint not implemented yet.")
            }
            get {
                // TODO: Implement get notifications
                call.respond(HttpStatusCode.NotImplemented, "Get notifications endpoint not implemented yet.")
            }
            get("/reade-notifications") {
                // TODO: Implement get read notifications
                call.respond(HttpStatusCode.NotImplemented, "Get read notifications endpoint not implemented yet.")
            }
        }
    }
}