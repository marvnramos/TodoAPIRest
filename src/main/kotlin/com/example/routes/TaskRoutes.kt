package com.example.routes

import com.example.models.task_models.Task
import com.example.services.TaskService
import com.example.services.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureTaskRoutes() {
    val taskImple = TaskService()
    val userImple = UserService()

    routing{
        get ("/v1/tasks"){
            try {
                val tasks: List<Task> = taskImple.allTasks()
                if(tasks.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound, "No tasks found")
                }
                call.respond(tasks)
            }catch(ex:Exception){
                call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
            }
        }
        post("/v1/tasks") {
            try{
                val requestBody: Task = call.receive<Task>()

                val user = userImple.getUser(requestBody.userId)

                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                }

                val task = taskImple.addTask(requestBody)

                call.respond(task!!)
            }catch (ex:Exception){
                println(ex.message)
                call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
            }
        }
        get("/v1/tasks/{id}"){
            if(call.parameters["id"] == null){
                call.respond(HttpStatusCode.BadRequest, "Task id is required")
            }

            val id = UUID.fromString(call.parameters["id"])
            val task: Task? = taskImple.getTask(id)

            if (task == null) {
                call.respond(HttpStatusCode.NotFound, "Task not found")
            }

            call.respond(task!!)
        }
    }
}