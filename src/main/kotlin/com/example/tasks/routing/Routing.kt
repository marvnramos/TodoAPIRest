package com.example.tasks.routing

import com.example.commons.dtos.ResDataDto
import com.example.commons.validation.HttpValidationHelper
import com.example.tasks.commands.CreateSharedTaskCommand
import com.example.tasks.commands.CreateTaskCommand
import com.example.tasks.commands.GetTasksCommand
import com.example.tasks.dtos.requests.AddRequestDto
import com.example.tasks.dtos.responses.TaskResponseDto
import com.example.tasks.middlewares.TaskMiddleware
import com.example.tasks.middlewares.TaskMiddleware.Companion.taskValidator
import com.example.tasks.repositories.implementation.TaskRepository
import com.example.tasks.repositories.implementation.UserTaskRepository
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
            route("/api/v1") {
                post("/tasks/store") {
                    try {
                        val principal = call.principal<JWTPrincipal>()
                        val payload = principal?.payload
                        val userId = UUID.fromString(payload?.getClaim("sub")?.asString())

                        val request = call.receive<AddRequestDto>()
                        taskValidator(request, taskMiddleware)

                        val command = CreateTaskCommand(
                            title = request.title,
                            description = request.description,
                            dueDate = request.dueDate ?: return@post,
                            statusId = request.statusId ?: 1,
                            priorityId = request.priorityId ?: 1,
                            createdBy = userId
                        )

                        val task = taskService.createTask(command) ?: throw Exception("Task not created")

                        val createUserTaskCommand = CreateSharedTaskCommand(userId, task.id!!)
                        userTaskService.createUserTask(createUserTaskCommand)

                        when {
                            request.sharedWith!!.isNotEmpty() -> {
                                val sharedWith = request.sharedWith
                                sharedWith.forEach {
                                    userTaskService.createUserTask(CreateSharedTaskCommand(it, task.id!!))
                                        ?: throw Exception("User tasks not created")
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
                    } catch (error: Exception) {
                        println(error)
                        call.respond(HttpStatusCode.InternalServerError, error.message ?: "An error occurred :/")
                    }
                }

                get("/tasks") {
                    val command = GetTasksCommand()

                    val tasks = taskService.getTasks(command)

                    val response = TaskResponseDto(
                        status = "success", message = "here are all task!", data = ResDataDto.Multiple(tasks)
                    )
                    call.respond(HttpStatusCode.OK, response)
                }
            }
        }
    }
}