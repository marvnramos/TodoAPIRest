package com.example.tasks.routing

import com.example.commons.dtos.ResDataDto
import com.example.tasks.commands.*
import com.example.tasks.domain.models.Task
import com.example.tasks.dtos.responses.TaskResponseDto
import com.example.tasks.middlewares.TaskMiddleware
import com.example.tasks.repositories.implementation.TaskRepository
import com.example.tasks.repositories.implementation.UserTaskRepository
import com.example.tasks.routing.handlers.personalTaskHandler
import com.example.tasks.routing.handlers.sharedTasksHandler
import com.example.tasks.routing.handlers.storeTaskHandler
import com.example.tasks.services.implementations.TasksServiceImpl
import com.example.tasks.services.implementations.UserTaskServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
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
            route("/api/v1/tasks") {
                post("/store") {
                    val command = HandleTaskCommand(
                        call,
                        taskService,
                        userTaskService,
                        taskMiddleware
                    )
                    storeTaskHandler(command)
                }

                get("/personal") {
                    val command = HandleTaskCommand(
                        call,
                        taskService,
                        userTaskService,
                        taskMiddleware
                    )
                    personalTaskHandler(command)
                }
                get("/shared") {
                    val command = HandleTaskCommand(
                        call,
                        taskService,
                        userTaskService,
                        taskMiddleware
                    )
                    sharedTasksHandler(command)
                }
                get("test") {
                    try {
                        val principal = call.principal<JWTPrincipal>()
                        val userId = principal?.payload?.getClaim("sub")?.asString()?.let { UUID.fromString(it) }

                        if (userId == null) {
                            call.respond(HttpStatusCode.BadRequest, "Invalid request payload")
                        }
                        val command = GetTasksCommand(userId!!)
                        val personalTasks = taskService.getTasks(command)

                        val personalTaskItems = personalTasks.map { task ->
                            Task(
                                id = task.id!!,
                                title = task.title,
                                description = task.description,
                                statusId = task.statusId,
                                priorityId = task.priorityId,
                                dueDate = task.dueDate,
                                createdBy = task.createdBy!!,
                                sharedWith = null,
                                createdAt = task.createdAt!!,
                                updatedAt = task.updatedAt!!
                            )
                        }

                        val sharedCommand = GetSharedWithTasksCommand(userId)
                        val tasksShared = userTaskService.getSharedTasks(sharedCommand)

                        val sharedTaskIds = tasksShared.map { it.taskId }
                        val whoImSharingWithCommand = GetWhoImSharingWIthCommand(userId, sharedTaskIds.first())
                        val sharedWith = userTaskService.getWhoImSharingWith(whoImSharingWithCommand)

                        val sharedTaskItems: List<Task> = sharedTaskIds.mapNotNull { taskId ->
                            val command = GetTaskByIdCommand(taskId)
                            taskService.getTaskById(command)?.let { task ->
                                Task(
                                    id = task.id!!,
                                    title = task.title,
                                    description = task.description,
                                    statusId = task.statusId,
                                    priorityId = task.priorityId,
                                    dueDate = task.dueDate,
                                    createdBy = task.createdBy!!,
                                    sharedWith = sharedWith.mapNotNull {
                                        if (it.userId != userId) it.userId else null
                                    },
                                    createdAt = task.createdAt!!,
                                    updatedAt = task.updatedAt!!
                                )
                            }
                        }

                        val taskList: List<Task> = personalTaskItems + sharedTaskItems

                        call.respond(
                            HttpStatusCode.OK, TaskResponseDto(
                                status = "success",
                                message = "Here are all tasks",
                                data = ResDataDto.Multiple(taskList)
                            )
                        )
                    } catch (error: Exception) {
                        println(error)
                        call.respond(
                            HttpStatusCode.InternalServerError, TaskResponseDto(
                                status = "error",
                                message = "An error occurred \n ${error.message}",
                                data = ResDataDto.Multiple(emptyList())
                            )
                        )
                    }
                }
                get("archived"){
                    // TODO: Get all archived tasks
                }
                get("/{id}") {
                    // TODO: Get task by id
                }
                patch("update/{id}") {
                    // TODO: Update task
                }
                delete("archive/{id}") {
                    // TODO: Archive task
                }
            }
        }
    }
}