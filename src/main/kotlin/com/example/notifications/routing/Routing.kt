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
            }
            patch("/mark-as-read/{id}") {
                // TODO: Implement mark as read
            }
            get {
                // TODO: Implement get notifications
            }
            get("/reade-notifications") {
                // TODO: Implement get read notifications
            }
        }
    }
}