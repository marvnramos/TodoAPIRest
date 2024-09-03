package com.example.tasks.services

import com.example.tasks.repositories.implementation.TaskRepository

class TasksServiceImpl (taskRepository: TaskRepository): TasksService(taskRepository)