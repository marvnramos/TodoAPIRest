package com.example.tasks.repositories.interfaces

import com.example.commons.interfaces.IEntityRepository
import com.example.tasks.domain.models.Task
import java.util.*

interface ITaskRepository : IEntityRepository<Task>{
    suspend fun getMyTasks(userId: UUID): List<Task>
}