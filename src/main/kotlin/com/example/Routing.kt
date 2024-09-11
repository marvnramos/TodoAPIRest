package com.example

import com.example.auth.routing.configureAuthRoutes
import com.example.tasks.routing.configureTaskRoutes
import com.example.users.routing.configureUsersRoutes
import io.ktor.server.application.*

fun Application.configureRouting(args: Array<String>) {
    configureTaskRoutes()
    configureUsersRoutes(args)
    configureAuthRoutes(args)
//    configureTaskRoutes()
//    configureUserRoutes()
//    configureAuthRoutes(args)
}
