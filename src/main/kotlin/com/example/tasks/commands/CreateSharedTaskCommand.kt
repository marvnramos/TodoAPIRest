package com.example.tasks.commands

import com.example.tasks.domain.models.Task
import java.time.Instant
import java.util.*

data class CreateSharedTaskCommand (
    val title: String,
    val description: String? = null,
    val dueDate: Instant,
    val statusId: Int? = null,
    val priorityId: Int? = null,
    val createdBy: UUID,
    val sharedWith: List<UUID>
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
            updatedAt = Instant.now(),
            sharedWith = sharedWith
        )
    }
}