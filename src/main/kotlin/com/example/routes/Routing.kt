package com.example.routes

import io.ktor.server.application.*

fun Application.configureRouting() {
    configureTaskRoutes()
    configureUserRoutes()
    configureAuthRoutes()
}
