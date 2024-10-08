package com.example.tasks.routing.handlers

import com.example.tasks.domain.models.Task
import java.util.*

fun toTaskHandler(task: Task, sharedWith: List<UUID>?): Task {
    return Task(
        id = task.id,
        title = task.title,
        description = task.description,
        statusId = task.statusId,
        priorityId = task.priorityId,
        dueDate = task.dueDate,
        createdBy = task.createdBy,
        sharedWith = sharedWith,
        createdAt = task.createdAt,
        updatedAt = task.updatedAt
    )
}