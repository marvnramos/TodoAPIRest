package com.example.tasks.commands

import com.example.tasks.domain.models.Task
import java.time.Instant
import java.util.*

data class CreateTaskCommand(
    val title: String,
    val description: String? = null,
    val dueDate: Instant? = null,
    val statusId: Int,
    val priorityId: Int,
    val createdBy: UUID,
) {
    fun toEntity(): Task {
        return Task(
            title = title,
            description = description,
            dueDate = dueDate,
            priorityId = priorityId,
            statusId = statusId,
            createdBy = createdBy,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )
    }
}