package com.example.tasks.services.interfaces

import com.example.tasks.domain.models.UserTask

interface IUserTaskService {
    suspend fun getSharedTasks(): List<UserTask>
    suspend fun getSharedTasksById(): List<UserTask>
    suspend fun getSharedTaskById(): UserTask? // USE 2 IDS
    suspend fun createSharedTask(): UserTask?
    suspend fun deleteSharedTask(): Boolean
    suspend fun archiveSharedTask(): Boolean
}