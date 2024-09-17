package com.example.tasks.services

import com.example.tasks.commands.CreateSharedTaskCommand
import com.example.tasks.commands.ArchiveTaskCommand
import com.example.tasks.domain.models.UserTask
import com.example.tasks.repositories.implementation.UserTaskRepository
import com.example.tasks.services.interfaces.IUserTaskService

abstract class UserTasksService(private val userTaskRepository: UserTaskRepository) : IUserTaskService {
    override suspend fun createSharedTask(command: CreateSharedTaskCommand): UserTask? {
        val userTask = command.toEntity()
        userTaskRepository.insert(userTask)
        return userTask
    }

    override suspend fun deleteSharedWith(command: CreateSharedTaskCommand): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun archiveTask(command: ArchiveTaskCommand): Boolean {
        val userTask = command.toEntity()
        return userTaskRepository.archive(userTask)
    }

    override suspend fun getSharedTasks(): List<UserTask> {
        TODO("Not yet implemented")
    }
}
