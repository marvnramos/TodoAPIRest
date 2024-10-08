package com.example.tasks.routing.handlers

import com.example.commons.dtos.ResDataDto
import com.example.tasks.commands.GetTasksCommand
import com.example.tasks.commands.HandleTaskCommand
import com.example.tasks.dtos.responses.TaskResponseDto
import com.example.tasks.routing.exception.exceptionHandler
import io.ktor.http.*
import io.ktor.server.response.*

suspend fun personalTaskHandler(
    personalHandlerCommand: HandleTaskCommand
) {
    val (call, taskService, _, _) = personalHandlerCommand
    exceptionHandler(call) {
        val userId = getAuthUserId(call)

        val command = GetTasksCommand(userId)
        val tasks = taskService.getTasks(command)

        call.respond(
            HttpStatusCode.OK, TaskResponseDto(
                status = "success",
                message = "here are all personal tasks! :)",
                data = ResDataDto.Multiple(tasks)
            )
        )
    }
}