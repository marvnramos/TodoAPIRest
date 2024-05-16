package com.example.routes

import io.ktor.server.application.*

fun Application.configureRouting(args: Array<String>) {
    configureTaskRoutes()
    configureUserRoutes()
    configureAuthRoutes(args)
}
