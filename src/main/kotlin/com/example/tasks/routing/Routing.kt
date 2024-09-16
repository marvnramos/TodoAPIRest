package com.example.tasks.routing

import com.example.commons.dtos.ResDataDto
import com.example.commons.validation.HttpValidationHelper
import com.example.tasks.commands.CreateTaskCommand
import com.example.tasks.commands.GetTasksCommand
import com.example.tasks.dtos.requests.AddRequestDto
import com.example.tasks.dtos.requests.AddSharedWithDto
import com.example.tasks.dtos.responses.TaskResponseDto
import com.example.tasks.middlewares.TaskMiddleware
import com.example.tasks.middlewares.TaskMiddleware.Companion.taskValidator
import com.example.tasks.repositories.implementation.TaskRepository
import com.example.tasks.services.implementations.TasksServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureTaskRoutes() {
    val taskRepository = TaskRepository()
    val taskService = TasksServiceImpl(taskRepository)
    val taskMiddleware = TaskMiddleware()
    routing {
        get("/hello") {
            call.respond(HttpStatusCode.OK, "hello")
        }
        authenticate("auth-jwt") {
            route("/api/v1") {
                post("/tasks/store") {
                    try {
                        val principal = call.principal<JWTPrincipal>()
                        val payload = principal?.payload
                        val userId = UUID.fromString(payload?.getClaim("sub")?.asString())

                        val request = call.receive<AddRequestDto>()
                        taskValidator(request, taskMiddleware)


                        val sharedRequest = if (request.sharedWith != null) {
                            request.toAddSharedWithDto()
                        } else null


                        if (sharedRequest?.sharedWith != null) {
                            val command = CreateTaskCommand(
                                title = sharedRequest.title,
                                description = sharedRequest.description!!,
                                dueDate = sharedRequest.dueDate!!,
                                status = sharedRequest.status!!,
                                priority = sharedRequest.priority!!,
                                createdBy = userId
                            )

                            val task = taskService.createTask(command)


                            val response = TaskResponseDto(
                                "success",
                                "Task created successfully",
                                ResDataDto.Single(task!!)
                            )

                            call.respond(HttpStatusCode.OK, response)
                            return@post
                        }

                        val command = CreateTaskCommand(
                            title = request.title,
                            description = request.description!!,
                            dueDate = request.dueDate!!,
                            status = request.status!!,
                            priority = request.priority!!,
                            createdBy = userId
                        )

                        val task = taskService.createTask(command)
                        val response = TaskResponseDto(
                            "success",
                            "Task created successfully",
                            ResDataDto.Single(task!!)
                        )

                        call.respond(HttpStatusCode.OK, response)
                    } catch (e: IllegalArgumentException) {
                        HttpValidationHelper.responseError(call, e.message ?: "Invalid data")
                    } catch (e: BadRequestException) {
                        when {
                            call.receive<AddSharedWithDto>().toString().isNullOrEmpty() -> {
                                HttpValidationHelper.responseError(call, "Invalid request payload")
                            }

                            else -> {
                                call.respond(HttpStatusCode.OK, "uwu")
                            }
                        }
//                        val request = call.receive<AddSharedWithDto>()
//                        if(request.sharedWith == null) {
//                            HttpValidationHelper.responseError(call, "Invalid request payload")
//                        }
//                        HttpValidationHelper.responseError(call, "Invalid request payload")
                    } catch (e: CannotTransformContentToTypeException) {
                        HttpValidationHelper.responseError(call, "Request payload required!")
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
                        data = ResDataDto.Multiple(tasks)
                    )
                    call.respond(HttpStatusCode.OK, response)
                }
            }
        }
    }
}