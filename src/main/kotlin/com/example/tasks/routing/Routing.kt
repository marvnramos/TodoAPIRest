package com.example.tasks.routing

import com.example.commons.models.ResData
import com.example.tasks.commands.CreateTaskCommand
import com.example.tasks.commands.GetTasksCommand
import com.example.tasks.domain.Status
import com.example.tasks.dtos.requests.AddRequestDto
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
import java.time.Instant
import java.util.*

fun Application.configureTaskRoutes() {
    val taskRepository = TaskRepository()
    val taskService = TasksServiceImpl(taskRepository)
    routing {
        get("/hello") {
            call.respond(HttpStatusCode.OK, "hello")
        }

        route("/api/v1") {
            post("/tasks/store") {
                try {
                    val request = call.receive<AddRequestDto>()
                    if (request.title.isBlank()) {
                        call.respond(HttpStatusCode.BadRequest, "Title is required")
                        return@post
                    }
//                    if(request.description.isBlank()){
//                        call.respond(HttpStatusCode.BadRequest, "Description is required")
//                        return@post
                    // description could be nullable
//                    }
                    if (request.dueDate.toString().isBlank()) {
                        call.respond(HttpStatusCode.BadRequest, "Due date is required")
                        return@post
                    }

                    if (request.createdBy.toString().isBlank()) {
                        TODO("get user id by auth token")
                    }

                    val command = CreateTaskCommand(
                        title = request.title,
                        description = request.description,
                        dueDate = request.dueDate,
                        status = request.status!!,
                        createdBy = UUID.randomUUID()
                    )

                    val task = taskService.createTask(command)


//                val command: CreateTaskCommand
//                val response = TaskResponseDto(
//                    "mi titulo",
//                    "mi descripción 123",
//
//
//                )
                } catch (error: Exception) {
                    println(error)
                    call.respond(HttpStatusCode.InternalServerError, "An error occurred :/")
                }
            }
            get("/tasks") {
                val command = GetTasksCommand()

                val tasks = taskService.getTasks(command)

                val response = TaskResponseDto(
                    status = "success",
                    message = "here are all task!",
                    data = ResData.Multiple(tasks)
                )
                call.respond(HttpStatusCode.OK, response)
            }
        }
    }
}