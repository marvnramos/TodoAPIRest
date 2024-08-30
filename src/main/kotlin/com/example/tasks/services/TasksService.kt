package com.example.tasks.services

import com.example.tasks.commands.CreateTaskCommand
import com.example.tasks.commands.DeleteTaskCommand
import com.example.tasks.commands.GetTasksCommand
import com.example.tasks.commands.UpdateTaskCommand
import com.example.tasks.domain.models.Task
import com.example.tasks.repositories.interfaces.ITaskRepository

abstract class TasksService(private val taskRepository: ITaskRepository) : ITasksService {
    override suspend fun getTasks(command: GetTasksCommand): List<Task> {
        return taskRepository.getAll()
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