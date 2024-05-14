package com.example.routes

import io.ktor.server.application.*
import com.example.routes.configureTaskRoutes
import com.example.routes.configureUserRoutes

fun Application.configureRouting() {
    configureTaskRoutes()
    configureUserRoutes()
}
