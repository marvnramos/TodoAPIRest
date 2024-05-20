package com.example.xd.routes

import io.ktor.server.application.*

fun Application.configureRouting(args: Array<String>) {
    configureTaskRoutes()
    configureUserRoutes()
    configureAuthRoutes(args)
}
