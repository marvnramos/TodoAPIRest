package com.example.tasks.services.implementations

import com.example.tasks.repositories.implementation.UserTaskRepository
import com.example.tasks.services.UserTasksService

class UserTaskServiceImpl(
    userTaskRepository: UserTaskRepository,
    tasksService: TasksServiceImpl
) : UserTasksService(userTaskRepository, tasksService)