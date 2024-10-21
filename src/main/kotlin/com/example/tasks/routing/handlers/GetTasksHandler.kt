package com.example.tasks.routing.handlers

import com.example.tasks.commands.*
import kotlinx.coroutines.runBlocking

suspend fun getTasksHandler(
    getAllHandlerCommand: HandleTaskCommand,
) {
    getAllTasksHandler(getAllHandlerCommand,
        extend = {
            runBlocking { listCleaner(it, getAllHandlerCommand.userTaskService, true) }
        }
    )
}

