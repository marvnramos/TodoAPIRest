package com.example.tasks.routing.handlers

import com.example.commons.dtos.ResDataDto
import com.example.tasks.commands.CreateSharedTaskCommand
import com.example.tasks.commands.CreateTaskCommand
import com.example.tasks.commands.HandleTaskCommand
import com.example.tasks.dtos.requests.AddRequestDto
import com.example.tasks.dtos.responses.TaskResponseDto
import com.example.tasks.middlewares.TaskMiddleware.Companion.taskValidator
import com.example.tasks.routing.exception.TaskException
import com.example.tasks.routing.exception.exceptionHandler
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.http.*
import io.ktor.server.auth.*
import java.util.*

suspend fun storeTaskHandler(
    storeHandlerCommand: HandleTaskCommand
) {
    val (call, taskService, userTaskService, taskMiddleware) = storeHandlerCommand
    exceptionHandler(call) {
        val principal = call.principal<JWTPrincipal>()
        val userId = UUID.fromString(principal?.payload?.getClaim("sub")?.asString())

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

        request.sharedWith?.forEach { sharedUserId ->
            userTaskService.createUserTask(CreateSharedTaskCommand(sharedUserId, task.id!!))
                ?: throw TaskException("User tasks not created")
        }

        call.respond(
            HttpStatusCode.Created, TaskResponseDto(
                status = "success",
                message = "Task created successfully",
                data = ResDataDto.Single(task)
            )
        )
    }

}
