package com.example.tasks.services

import com.example.tasks.commands.CreateTaskCommand
import com.example.tasks.commands.DeleteTaskCommand
import com.example.tasks.commands.GetTaskCommand
import com.example.tasks.commands.UpdateTaskCommand
import com.example.tasks.domain.models.Task

interface ITasksService {
    suspend fun getTasks(): List<Task>
    suspend fun getTask(command: GetTaskCommand): Task?
    suspend fun createTask(command: CreateTaskCommand): Task?
    suspend fun updateTask(command: UpdateTaskCommand): Boolean
    suspend fun deleteTask(command: DeleteTaskCommand): Boolean
}