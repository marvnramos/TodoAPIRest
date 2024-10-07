package com.example

import com.example.auth.routing.configureAuthRoutes
import com.example.notifications.routing.configureNotificationsRouting
import com.example.tasks.routing.configureTaskRoutes
import com.example.users.routing.configureUsersRoutes
import io.ktor.server.application.*

fun Application.configureRouting(args: Array<String>) {
    configureTaskRoutes()
    configureUsersRoutes(args)
    configureAuthRoutes(args)
    configureNotificationsRouting()
//    configureTaskRoutes()
//    configureUserRoutes()
//    configureAuthRoutes(args)
}
