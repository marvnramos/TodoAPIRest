package com.example.dao.implementations

import com.example.dao.interfaces.DAOTask
import com.example.models.task_models.Task
import java.util.*

class DAOTaskImpl:DAOTask {
    override suspend fun allTasks(): List<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(id: UUID): Task? {
        TODO("Not yet implemented")
    }

    override suspend fun addTask(task: Task): Task? {
        TODO("Not yet implemented")
    }

    override suspend fun editTask(id: UUID, task: Task?): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(id: UUID): Boolean {
        TODO("Not yet implemented")
    }

}