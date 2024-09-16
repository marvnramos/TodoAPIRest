package com.example.tasks.services

import com.example.tasks.repositories.implementation.UserTaskRepository
import com.example.tasks.services.interfaces.IUserTaskService

abstract class UserTasksService(private val userTaskRepository: UserTaskRepository): IUserTaskService
