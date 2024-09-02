package com.example.tasks.routing

import com.example.tasks.commands.CreateTaskCommand
import com.example.tasks.commands.GetTasksCommand
import com.example.tasks.domain.Status
import com.example.tasks.domain.models.Task
import com.example.tasks.dtos.responses.TaskResponseDto
import com.example.tasks.repositories.implementation.TaskRepository
import com.example.tasks.services.TasksServiceImpl
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.*

fun Application.configureTaskRoutes() {
    val taskRepository = TaskRepository()
    val taskService = TasksServiceImpl(taskRepository)
    routing {
        get("/hello") {
            call.respond(HttpStatusCode.OK, "hello")
        }

        route("/api/v1"){
            post("/tasks/store"){
//                val command: CreateTaskCommand
//                val response = TaskResponseDto(
//                    "mi titulo",
//                    "mi descripción 123",
//
//
//                )
            }
            get("/tasks"){
                val command = GetTasksCommand()

                val resonse = TaskResponseDto(
                    status = "success",
                    message = "Here are all tasks!",
                    data = command
                )

                call.respond(HttpStatusCode.OK, resonse)
            }
        }
    }
}