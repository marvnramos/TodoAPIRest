package com.example.tasks.repositories.interfaces

import com.example.tasks.domain.models.UserTask
import java.util.*

interface IUserTaskRepository {
    suspend fun insert(entity: UserTask): UserTask?
    suspend fun replace(entity: UserTask): Boolean
    suspend fun delete(entity: UserTask): Boolean
    suspend fun archive(entity: UserTask): Boolean
    suspend fun getMySharedTasks(id: UUID): List<UserTask>
    suspend fun getUserTasksByTaskId(id: UUID): List<UserTask>
    suspend fun getAllRelatedTasks(id: UUID): List<UserTask>
    suspend fun getWhoImSharingWith(userId: UUID, taskId: UUID): List<UserTask>
}