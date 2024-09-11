package com.example

import com.example.tasks.routing.configureTaskRoutes
import com.example.users.routing.configureUsersRoutes
import io.ktor.server.application.*

fun Application.configureRouting(args: Array<String>) {
    configureTaskRoutes()
    configureUsersRoutes(args)
//    configureTaskRoutes()
//    configureUserRoutes()
//    configureAuthRoutes(args)
}
