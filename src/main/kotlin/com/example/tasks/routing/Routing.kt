package com.example.tasks.routing

import com.example.commons.dtos.ResDataDto
import com.example.commons.validation.HttpValidationHelper
import com.example.tasks.commands.*
import com.example.tasks.dtos.requests.AddRequestDto
import com.example.tasks.dtos.responses.TaskResponseDto
import com.example.tasks.middlewares.TaskMiddleware
import com.example.tasks.middlewares.TaskMiddleware.Companion.taskValidator
import com.example.tasks.repositories.implementation.TaskRepository
import com.example.tasks.repositories.implementation.UserTaskRepository
import com.example.tasks.routing.exception.TaskException
import com.example.tasks.services.implementations.TasksServiceImpl
import com.example.tasks.services.implementations.UserTaskServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Application.configureTaskRoutes() {
    val taskRepository = TaskRepository()
    val taskService = TasksServiceImpl(taskRepository)
    val taskMiddleware = TaskMiddleware()

    val userTaskRepository = UserTaskRepository()
    val userTaskService = UserTaskServiceImpl(userTaskRepository, taskService)
    routing {
        get("/hello") {
            call.respond(HttpStatusCode.OK, "hello")
        }
        authenticate("auth-jwt") {
            route("/api/v1/tasks") {
                post("/store") {
                    try {
                        val principal = call.principal<JWTPrincipal>()
                        val payload = principal?.payload
                        val userId = UUID.fromString(payload?.getClaim("sub")?.asString())

                        val request = call.receive<AddRequestDto>()
                        taskValidator(request, taskMiddleware)

                        val command = CreateTaskCommand(
                            title = request.title,
                            description = request.description,
                            dueDate = request.dueDate,
                            statusId = request.statusId ?: 1,
                            priorityId = request.priorityId ?: 1,
                            createdBy = userId
                        )

                        val task = taskService.createTask(command) ?: throw TaskException("Task not created")

                        val createUserTaskCommand = CreateSharedTaskCommand(userId, task.id!!)
                        userTaskService.createUserTask(createUserTaskCommand)

                        when {
                            request.sharedWith!!.isNotEmpty() -> {
                                val sharedWith = request.sharedWith
                                sharedWith.forEach {
                                    userTaskService.createUserTask(CreateSharedTaskCommand(it, task.id!!))
                                        ?: throw TaskException("User tasks not created")
                                }
                            }
                        }

                        call.respond(HttpStatusCode.OK, "ajdj")
                    } catch (e: IllegalArgumentException) {
                        HttpValidationHelper.responseError(call, e.message ?: "Invalid data")
                    } catch (e: BadRequestException) {
                        HttpValidationHelper.responseError(call, "Invalid request payload")
                    } catch (e: CannotTransformContentToTypeException) {
                        HttpValidationHelper.responseError(call, "Request payload required!")
                    } catch (e: TaskException) {
                        call.respond(
                            HttpStatusCode.InternalServerError, TaskResponseDto(
                                status = "error",
                                message = e.message ?: "An error occurred",
                                data = ResDataDto.Multiple(emptyList())
                            )
                        )
                    } catch (error: Exception) {
                        println(error)
                        call.respond(
                            HttpStatusCode.InternalServerError, TaskResponseDto(
                                status = "error",
                                message = "An error occurred",
                                data = ResDataDto.Multiple(emptyList())
                            )
                        )
                    }
                }

                get("/personal") {
                    try {
                        val principal = call.principal<JWTPrincipal>()
                        val payload = principal?.payload
                        val userId = UUID.fromString(payload?.getClaim("sub")?.asString())

                        val command = GetTasksCommand(userId)
                        val tasks = taskService.getTasks(command)

                        call.respond(
                            HttpStatusCode.OK, TaskResponseDto(
                                status = "success",
                                message = "here are all personal tasks! :)",
                                data = ResDataDto.Multiple(tasks)
                            )
                        )

                    } catch (e: BadRequestException) {
                        HttpValidationHelper.responseError(call, "Invalid request payload")
                    } catch (error: Exception) {
                        println(error)
                        call.respond(
                            HttpStatusCode.InternalServerError, TaskResponseDto(
                                status = "error",
                                message = "An error occurred",
                                data = ResDataDto.Multiple(emptyList())
                            )
                        )
                    }
                }
                get("/shared") {
                    try {
                        val principal = call.principal<JWTPrincipal>()
                        val payload = principal?.payload
                        val userId = UUID.fromString(payload?.getClaim("sub")?.asString())

                        val command = GetSharedWithTasksCommand(userId)
                        val tasks = userTaskService.getSharedTasks(command)

                        call.respond(
                            HttpStatusCode.OK, Json.encodeToString(tasks)
                        )

                    } catch (e: BadRequestException) {
                        HttpValidationHelper.responseError(call, "Invalid request payload")
                    } catch (error: Exception) {
                        println(error)
                        call.respond(
                            HttpStatusCode.InternalServerError, TaskResponseDto(
                                status = "error",
                                message = "An error occurred",
                                data = ResDataDto.Multiple(emptyList())
                            )
                        )
                    }
                }
                get("test") {
                    try {
                        val command = GetByTaskIdCommand(UUID.fromString("07f017c9-5fc8-404b-87a1-c0e3b17f80cf"))
                        val tasks = userTaskService.getSharedTasksByTaskId(command)

                        call.respond(
                            HttpStatusCode.OK, Json.encodeToString(tasks)
                        )
                    } catch (error: Exception) {
                        println(error)
                        call.respond(
                            HttpStatusCode.InternalServerError, TaskResponseDto(
                                status = "error",
                                message = "An error occurred",
                                data = ResDataDto.Multiple(emptyList())
                            )
                        )
                    }
                }
            }
        }
    }
}