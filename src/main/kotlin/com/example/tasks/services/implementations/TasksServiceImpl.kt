package com.example.tasks.services.implementations

import com.example.tasks.repositories.implementation.TaskRepository
import com.example.tasks.services.TasksService

class TasksServiceImpl (taskRepository: TaskRepository): TasksService(taskRepository)