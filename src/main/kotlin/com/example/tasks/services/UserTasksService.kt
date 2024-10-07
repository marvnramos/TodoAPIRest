package com.example.tasks.services

import com.example.tasks.commands.*
import com.example.tasks.domain.models.UserTask
import com.example.tasks.repositories.implementation.UserTaskRepository
import com.example.tasks.services.implementations.TasksServiceImpl
import com.example.tasks.services.interfaces.IUserTaskService

abstract class UserTasksService(
    private val userTaskRepository: UserTaskRepository,
    private val tasksService: TasksServiceImpl
) : IUserTaskService {
    override suspend fun createUserTask(command: CreateSharedTaskCommand): UserTask? {
        val userTask = command.toEntity()
        userTaskRepository.insert(userTask)
        return userTask
    }

    override suspend fun deleteSharedWith(command: DeleteSharedWithFromTasksCommand): Boolean {
        val commandTask = GetTaskByIdCommand(command.taskId)
        val task = tasksService.getTaskById(commandTask)
        if (task != null) {
            if (task.createdBy == command.userId) {
                val entity = command.toEntity()
                return userTaskRepository.delete(entity)

            }
        }
        return false
    }

    override suspend fun getAllMyTasks(command: GetAllMyTasksCommand): List<UserTask> {
        return userTaskRepository.getAllRelatedTasks(command.userId)
    }

    override suspend fun getSharedTasksByTaskId(command: GetByTaskIdCommand): List<UserTask> {
        val userTasks = userTaskRepository.getUserTasksByTaskId(command.taskId)
        return userTasks
    }

    override suspend fun archiveTask(command: ArchiveTaskCommand): Boolean {
        val userTask = command.toEntity()
        return userTaskRepository.archive(userTask)
    }

    override suspend fun getWhoImSharingWith(command: GetWhoImSharingWIthCommand): List<UserTask> {
        return userTaskRepository.getWhoImSharingWith(command.userId, command.taskId)
    }

    override suspend fun getSharedTasks(command: GetSharedWithTasksCommand): List<UserTask> {
        val sharedTasks = userTaskRepository.getMySharedTasks(command.userId)
        return sharedTasks
    }
}
