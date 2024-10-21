package com.example.tasks.routing.handlers

import com.example.tasks.commands.GetByTaskIdCommand
import com.example.tasks.domain.models.Task
import com.example.tasks.services.implementations.UserTaskServiceImpl

suspend fun listCleaner(taskList: List<Task>, userTaskService: UserTaskServiceImpl, notArchived: Boolean): List<Task> {
    val cleanedList = taskList.filter { task ->
        val command = task.id?.let { GetByTaskIdCommand(it) }
        command != null && userTaskService
            .getSharedTasksByTaskId(command)
            .any { userTask ->
                if (notArchived)
                    userTask.archivedAt == null
                else
                    userTask.archivedAt != null
            }
    }
    return cleanedList
}