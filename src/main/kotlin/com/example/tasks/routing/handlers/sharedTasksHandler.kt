package com.example.tasks.routing.handlers

import com.example.commons.dtos.ResDataDto
import com.example.tasks.commands.GetSharedWithTasksCommand
import com.example.tasks.commands.GetTaskByIdCommand
import com.example.tasks.commands.GetWhoImSharingWIthCommand
import com.example.tasks.commands.HandleTaskCommand
import com.example.tasks.dtos.responses.TaskResponseDto
import com.example.tasks.routing.exception.TaskExceptionHandler
import io.ktor.http.*
import io.ktor.server.response.*

suspend fun sharedTasksHandler(
    sharedHandlerCommand: HandleTaskCommand,
    taskException: TaskExceptionHandler = TaskExceptionHandler()
) {
    val (call, taskService, userTaskService, _) = sharedHandlerCommand
    taskException.exceptionHandler(call) {
        val userId = getAuthUserId(call)

        val sharedTasks = userTaskService.getSharedTasks(GetSharedWithTasksCommand(userId))
        val sharedTaskIds = sharedTasks.map { it.taskId }

        val sharedTaskList = sharedTaskIds.mapNotNull { taskId ->
            val task = taskService.getTaskById(GetTaskByIdCommand(taskId))
            task?.let {
                val command = GetWhoImSharingWIthCommand(userId, taskId)
                val sharedWithUsers = userTaskService.getWhoImSharingWith(command)
                    .mapNotNull { sharingInfo -> if (sharingInfo.userId != userId) sharingInfo.userId else null }

                toTaskHandler(task, sharedWithUsers)
            }
        }

        call.respond(
            HttpStatusCode.OK, TaskResponseDto(
                status = "success",
                message = "Here are tasks which you shared with others",
                data = ResDataDto.Multiple(sharedTaskList)
            )
        )
    }
}


