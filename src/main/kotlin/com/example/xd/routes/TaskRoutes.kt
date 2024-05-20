package com.example.xd.routes

import com.example.models.task_models.Task
import com.example.models.task_models.TaskPartial
import com.example.services.TaskService
import com.example.services.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureTaskRoutes() {
    val taskImple = TaskService()
    val userImple = UserService()

    routing {
        authenticate("auth-jwt") {

            get("/v1/tasks") {
                try {
                    val tasks: List<Task> = taskImple.allTasks()
                    if (tasks.isEmpty()) {
                        call.respond(HttpStatusCode.NotFound, "No tasks found")
                    }
                    call.respond(tasks)
                } catch (ex: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
                }
            }

            post("/v1/tasks") {
                try {
                    val principal = call.principal<JWTPrincipal>()
                    val payload = principal?.payload
                    val userId = payload?.getClaim("userId")?.asString()

                    if (userId.isNullOrEmpty()) {
                        call.respond(HttpStatusCode.BadRequest, "User ID is invalid")
                        return@post
                    }

                    val id = UUID.fromString(userId)
                    val user = userImple.getUser(id)

                    if (user == null) {
                        call.respond(HttpStatusCode.NotFound, "User not found")
                        return@post
                    }

                    val requestBody: Task = call.receive()
                    requestBody.userId = id

                    val task = taskImple.addTask(requestBody)
                    if (task != null) {
                        call.respond(task)
                    } else {
                        call.respond(HttpStatusCode.InternalServerError, "Failed to create task")
                    }
                } catch (ex: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Internal Server Error: ${ex.message}")
                }
            }

            get("/v1/tasks/{id}") {
                try {
                    val id = call.parameters["id"]?.let { UUID.fromString(it) }

                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, "Task id is required")
                        return@get
                    }

                    val task: Task? = taskImple.getTask(id)

                    if (task == null) {
                        call.respond(HttpStatusCode.NotFound, "Task not found")
                    } else {
                        call.respond(task)
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
                }
            }



            delete("/v1/tasks/{id}") {
                try {
                    if (call.parameters["id"].isNullOrEmpty()) {
                        call.respond(HttpStatusCode.BadRequest, "Task id is required")
                    }
                    val id = UUID.fromString(call.parameters["id"])
                    val task: Task? = taskImple.getTask(id)
                    if (task == null) {
                        call.respond(HttpStatusCode.NotFound, "Task not found")
                    }

                    taskImple.deleteTask(id)

                    call.respond(task!!)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
                }
            }
            patch("/v1/tasks/{id}") {
                try {
                    val id = call.parameters["id"]?.let { it1 -> UUID.fromString(it1) }
                    val requestBody: TaskPartial = call.receive<TaskPartial>()

                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, "Task id is required")
                        return@patch
                    }
                    val task: Boolean = taskImple.editTask(id, requestBody)

                    if (!task) {
                        call.respond(HttpStatusCode.NotModified, "Something failed. Task not modified")
                    }

                    call.respond(HttpStatusCode.OK, "Task successfully updated")
                } catch (ex: Exception) {
                    println(ex.message)
                    call.respond(HttpStatusCode.InternalServerError, "${ex.message}")
                }
            }
        }
    }
}