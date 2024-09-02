package com.example

import com.example.tasks.routing.configureTaskRoutes
import io.ktor.server.application.*

fun Application.configureRouting() {
    configureTaskRoutes()
//    configureTaskRoutes()
//    configureUserRoutes()
//    configureAuthRoutes(args)
}
