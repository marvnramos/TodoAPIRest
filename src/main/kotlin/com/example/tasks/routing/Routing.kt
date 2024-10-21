package com.example.tasks.routing

import com.example.commons.dtos.ResDataDto
import com.example.tasks.commands.*
import com.example.tasks.dtos.responses.TaskResponseDto
import com.example.tasks.middlewares.TaskMiddleware
import com.example.tasks.repositories.implementation.TaskRepository
import com.example.tasks.repositories.implementation.UserTaskRepository
import com.example.tasks.routing.handlers.*
import com.example.tasks.services.implementations.TasksServiceImpl
import com.example.tasks.services.implementations.UserTaskServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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
                fun createHandleTaskCommand(call: ApplicationCall): HandleTaskCommand {
                    return HandleTaskCommand(
                        call = call,
                        taskService = taskService,
                        userTaskService = userTaskService,
                        taskMiddleware = taskMiddleware
                    )
                }

                post("/store") {
                    storeTaskHandler(createHandleTaskCommand(call))
                }

                get("/personal") {
                    personalTaskHandler(createHandleTaskCommand(call))
                }

                get("/shared") {
                    sharedTasksHandler(createHandleTaskCommand(call))
                }

                get("/") {
                    getTasksHandler(createHandleTaskCommand(call))
                }
                get("/filter"){
                    /**
                     * filter by status: /api/v1/tasks/filter?statusId=1
                     * filter by priority: /api/v1/tasks/filter?priorityId=2
                     * filter both: /api/v1/tasks/filter?statusId=1&priorityId=2
                     * without filters (return exception error): /api/v1/tasks/filter
                     */
                    call.respond(HttpStatusCode.NotImplemented, "Filter tasks endpoint not implemented yet.")
                }
                get("/archived") {
                    getArchivedTasksHandler(createHandleTaskCommand(call))
                }

                get("/{id}") {
                    // Placeholder for fetching a task by id
                    call.respond(HttpStatusCode.NotImplemented, "Get task by ID endpoint not implemented yet.")
                }

                patch("/update/{id}") {
                    // Placeholder for task update logic
                    call.respond(HttpStatusCode.NotImplemented, "Update task endpoint not implemented yet.")
                }

                delete("/archive/{id}") {
                    // Placeholder for task archive logic
                    call.respond(HttpStatusCode.NotImplemented, "Archive task endpoint not implemented yet.")
                }

                // Route for testing error handling
                get("/test") {
                    try {
                        // logic that may throw an exception (- -)"
                        call.respond(HttpStatusCode.OK, "Test route successful!")
                    } catch (error: Exception) {
                        call.respond(
                            HttpStatusCode.InternalServerError, TaskResponseDto(
                                status = "error",
                                message = "An error occurred: ${error.message}",
                                data = ResDataDto.Multiple(emptyList())
                            )
                        )
                    }
                }
            }
        }
    }
}
