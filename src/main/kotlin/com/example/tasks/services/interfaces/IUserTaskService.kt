package com.example.tasks.services.interfaces

import com.example.tasks.commands.*
import com.example.tasks.domain.models.UserTask

interface IUserTaskService {
    suspend fun createUserTask(command: CreateSharedTaskCommand): UserTask?
    suspend fun deleteSharedWith(command: DeleteSharedWithFromTasksCommand): Boolean
    suspend fun archiveTask(command: ArchiveTaskCommand): Boolean
    suspend fun getSharedTasks(command: GetSharedWithTasksCommand): List<UserTask>
    suspend fun getSharedTasksByTaskId(command: GetByTaskIdCommand): List<UserTask>
    suspend fun getAllMyTasks(command: GetAllMyTasksCommand): List<UserTask>
    suspend fun getWhoImSharingWith(command: GetWhoImSharingWIthCommand): List<UserTask>
//    suspend fun getSharedTasks(): List<UserTask>
//    suspend fun getSharedTasksById(): List<UserTask>
//    suspend fun getSharedTaskById(): UserTask? // USE 2 IDS
//    suspend fun createSharedTask(): UserTask?
//    suspend fun deleteSharedTask(): Boolean
//    suspend fun archiveSharedTask(): Boolean
}