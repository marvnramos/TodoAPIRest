package com.example.tasks.routing.handlers

import com.example.commons.dtos.ResDataDto
import com.example.tasks.commands.*
import com.example.tasks.domain.models.Task
import com.example.tasks.dtos.responses.TaskResponseDto
import com.example.tasks.routing.exception.exceptionHandler
import io.ktor.http.*
import io.ktor.server.response.*

suspend fun getTasksHandler(storeHandlerCommand: HandleTaskCommand) {
    val (call, taskService, userTaskService, _) = storeHandlerCommand
    exceptionHandler(call) {
        val userId = getAuthUserId(call)

        val personalTasks = taskService.getTasks(GetTasksCommand(userId))
        val personalTaskItems = personalTasks.map { mapTaskHandler(it, null) }

        val sharedTasks = userTaskService.getSharedTasks(GetSharedWithTasksCommand(userId))
        val sharedTaskItems = sharedTasks.mapNotNull { sharedTask ->
            val task = taskService.getTaskById(GetTaskByIdCommand(sharedTask.taskId))
            task?.let { it ->
                val sharedWithUsers = userTaskService.getWhoImSharingWith(
                    GetWhoImSharingWIthCommand(userId, sharedTask.taskId)
                ).mapNotNull { if (it.userId != userId) it.userId else null }

                mapTaskHandler(it, sharedWithUsers)
            }
        }

        val taskList: List<Task> = personalTaskItems + sharedTaskItems

        call.respond(
            HttpStatusCode.OK, TaskResponseDto(
                status = "success",
                message = "Here are all tasks.",
                data = ResDataDto.Multiple(taskList)
            )
        )
    }
}
