package com.example.routes

import com.example.services.TaskService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureTaskRoutes() {
    val taskImple = TaskService()

    routing{
        get("/v1/task") {
            call.respond("taskImple")
        }
    }
}