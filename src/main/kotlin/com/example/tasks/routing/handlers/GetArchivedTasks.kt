package com.example.tasks.routing.handlers

import com.example.tasks.commands.HandleTaskCommand
import kotlinx.coroutines.runBlocking

suspend fun getArchivedTasksHandler(
    getArchivedHandlerCommand: HandleTaskCommand,
) {
    getAllTasksHandler(getArchivedHandlerCommand,
        extend = {
            runBlocking { listCleaner(it, getArchivedHandlerCommand.userTaskService, false) }
        }
    )
}
