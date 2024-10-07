package com.example.tasks.routing

import com.example.commons.dtos.ResDataDto
import com.example.commons.validation.HttpValidationHelper
import com.example.tasks.commands.*
import com.example.tasks.domain.models.SharedTask
import com.example.tasks.domain.models.TaskItem
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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
                        val principal = call.principal<JWTPrincipal>()
                        val payload = principal?.payload
                        val userId = UUID.fromString(payload?.getClaim("sub")?.asString())

                        val command = GetTasksCommand(userId)
                        val personalTasks = taskService.getTasks(command)

                        val personalTaskItems = personalTasks.map { task ->
                            TaskItem.PersonalTask(
                                task = task
                            )
                        }

                        val sharedCommand = GetSharedWithTasksCommand(userId)
                        val tasksShared = userTaskService.getSharedTasks(sharedCommand)

                        val sharedTaskIds = tasksShared.map { it.taskId }

                        val sharedTaskItems: List<TaskItem.SharedTask> = sharedTaskIds.mapNotNull { taskId ->
                            val command = GetTaskByIdCommand(taskId)
                            taskService.getTaskById(command)?.let { task ->
                                TaskItem.SharedTask(
                                    task = SharedTask(
                                        id = task.id!!,
                                        title = task.title,
                                        description = task.description,
                                        statusId = task.statusId,
                                        priorityId = task.priorityId,
                                        dueDate = task.dueDate,
                                        createdBy = task.createdBy!!,
                                        sharedWith = sharedTaskIds, // fix this
                                        createdAt = task.createdAt!!,
                                        updatedAt = task.updatedAt!!
                                    )
                                )
                            }
                        }

                        val taskList: List<TaskItem> = personalTaskItems + sharedTaskItems

                        call.respond(HttpStatusCode.OK, taskList)
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

            }
        }
    }
}