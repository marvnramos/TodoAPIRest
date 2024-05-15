package com.example.dao.interfaces

import com.example.models.task_models.Task
import com.example.models.task_models.TaskPartial
import java.util.UUID

interface DAOTask {
    suspend fun allTasks(): List<Task>
    suspend fun getTask(id: UUID): Task?
    suspend fun addTask(task: Task): Task?
    suspend fun editTask(id: UUID, task: TaskPartial): Boolean
    suspend fun deleteTask(id: UUID): Boolean
}