package com.example.tasks.services

import com.example.tasks.domain.entities.PartialTask
import com.example.tasks.domain.entities.Task
import java.util.UUID

interface ITasksService {
    suspend fun getTasks(): List<Task>
    suspend fun getTask(id: UUID): Task?
    suspend fun createTask(task: Task): Task?
    suspend fun updateTask(id: UUID, task: PartialTask): Boolean
    suspend fun deleteTask(id: UUID): Boolean
}