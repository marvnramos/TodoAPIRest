package com.example.tasks.commands

import com.example.tasks.middlewares.TaskMiddleware
import com.example.tasks.services.TasksService
import com.example.tasks.services.implementations.UserTaskServiceImpl
import io.ktor.server.application.*

data class HandleTaskCommand(
    val call: ApplicationCall,
    val taskService: TasksService,
    val userTaskService: UserTaskServiceImpl,
    val taskMiddleware: TaskMiddleware,
)