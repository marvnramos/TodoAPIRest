package com.example.tasks.services

import com.example.tasks.commands.*
import com.example.tasks.domain.models.Task
import com.example.tasks.repositories.implementation.TaskRepository
import com.example.tasks.services.interfaces.ITasksService

abstract class TasksService(private val taskRepository: TaskRepository) : ITasksService {
    override suspend fun getTasks(command: GetTasksCommand): List<Task> {
        return taskRepository.getAll()
    }

    override suspend fun getTaskById(command: GetTaskByIdCommand): Task? {
        return taskRepository.findById(command.id)
    }

    override suspend fun getOneTask(command: GetOneTaskCommand): Task? {
        return taskRepository.find(command.predicate)
    }

    override suspend fun createTask(command: CreateTaskCommand): Task? {
        val task = command.toEntity()
        taskRepository.insert(task)
        return task
    }

    override suspend fun updateTask(command: UpdateTaskCommand): Task? {
        val task = taskRepository.findById(command.id) ?: return null
        command.updateEntity(task)
        return task
    }

    override suspend fun deleteTask(command: DeleteTaskCommand): Boolean {
        taskRepository.delete(command.id)
        return true
    }
}