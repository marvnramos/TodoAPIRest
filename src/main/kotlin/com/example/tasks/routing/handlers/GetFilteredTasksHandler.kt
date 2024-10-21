package com.example.tasks.routing.handlers

import com.example.tasks.commands.GetTaskByIdCommand
import com.example.tasks.commands.HandleTaskCommand
import com.example.tasks.domain.models.Task
import com.example.tasks.services.TasksService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking

suspend fun getFilteredTasksHandler(
    handleTaskCommand: HandleTaskCommand,
) {
    getAllTasksHandler(handleTaskCommand,
        extend = {
            runBlocking {
                filtersTasks(it, handleTaskCommand.taskService, handleTaskCommand.call, Filter(1, 1))
            }
        })
}


private suspend fun filtersTasks(
    taskList: List<Task>,
    taskService: TasksService,
    call: ApplicationCall,
    filters: Filter
): List<Task?> {
    return taskList.filter { task ->
        val taskId = task.id ?: return@filter false
        val fetchedTask = taskService.getTaskById(GetTaskByIdCommand(taskId))

        fetchedTask != null && when {
            filters.status != null && filters.priority != null ->
                fetchedTask.statusId == filters.status && fetchedTask.priorityId == filters.priority
            filters.status != null ->
                fetchedTask.statusId == filters.status
            filters.priority != null ->
                fetchedTask.priorityId == filters.priority

            else -> {
                call.respond(HttpStatusCode.BadRequest, "Invalid filter")
                return emptyList()
            }
        }
    }
}


data class Filter(
    val status: Int? = null,
    val priority: Int? = null
)