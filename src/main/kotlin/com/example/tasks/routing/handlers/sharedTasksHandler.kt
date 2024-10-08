package com.example.tasks.routing.handlers

import com.example.commons.dtos.ResDataDto
import com.example.tasks.commands.GetSharedWithTasksCommand
import com.example.tasks.commands.GetTaskByIdCommand
import com.example.tasks.commands.GetWhoImSharingWIthCommand
import com.example.tasks.commands.HandleTaskCommand
import com.example.tasks.domain.models.Task
import com.example.tasks.dtos.responses.TaskResponseDto
import com.example.tasks.routing.exception.exceptionHandler
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import java.util.*

suspend fun sharedTasksHandler(sharedHandlerCommand: HandleTaskCommand) {
    val (call, taskService, userTaskService, _) = sharedHandlerCommand
    exceptionHandler(call) {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal?.payload?.getClaim("sub")?.asString()?.let { UUID.fromString(it) }

        if (userId == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid or missing userId.")
            return@exceptionHandler
        }

        val sharedTasks = userTaskService.getSharedTasks(GetSharedWithTasksCommand(userId))
        val sharedTaskIds = sharedTasks.map { it.taskId }

        val sharedTaskList = sharedTaskIds.mapNotNull { taskId ->
            val task = taskService.getTaskById(GetTaskByIdCommand(taskId))
            task?.let {
                val command = GetWhoImSharingWIthCommand(userId, taskId)
                val sharedWithUsers = userTaskService.getWhoImSharingWith(command)
                    .mapNotNull { sharingInfo -> if (sharingInfo.userId != userId) sharingInfo.userId else null }

                mapTask(task, sharedWithUsers)
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

private fun mapTask(task: Task, sharedWith: List<UUID>?): Task {
    return Task(
        id = task.id,
        title = task.title,
        description = task.description,
        statusId = task.statusId,
        priorityId = task.priorityId,
        dueDate = task.dueDate,
        createdBy = task.createdBy,
        sharedWith = sharedWith,
        createdAt = task.createdAt,
        updatedAt = task.updatedAt
    )
}
