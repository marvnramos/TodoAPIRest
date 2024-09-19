package com.example.tasks.commands

import com.example.tasks.domain.models.Task
import java.time.Instant
import java.util.*

data class UpdateTaskCommand(
    val id: UUID,
    val title: String? = null,
    val description: String? = null,
    val dueDate: Instant? = null,
    val statusId: Int,
    val priorityId: Int
) {
    fun updateEntity(task: Task): Task {
        return task.copy(
            title = this.title ?: task.title,
            description = this.description ?: task.description,
            dueDate = this.dueDate ?: task.dueDate,
            statusId = this.statusId ?: task.statusId,
            priorityId = this.priorityId ?: task.priorityId,
            updatedAt = Instant.now()
        )
    }
}
