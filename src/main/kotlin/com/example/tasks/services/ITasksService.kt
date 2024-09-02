package com.example.tasks.services

import com.example.tasks.commands.*
import com.example.tasks.domain.models.Task

interface ITasksService {
    suspend fun getTasks(command: GetTasksCommand): List<Task>
    suspend fun getTaskById(command: GetTaskByIdCommand): Task?
    suspend fun getOneTask(command: GetOneTaskCommand): Task?
    suspend fun createTask(command: CreateTaskCommand): Task?
    suspend fun updateTask(command: UpdateTaskCommand): Task?
    suspend fun deleteTask(command: DeleteTaskCommand): Boolean
}