package com.example.tasks.commands

import com.example.tasks.domain.models.UserTask
import java.util.UUID

data class ArchiveTaskCommand(
    val taskId: UUID,
    val userId: UUID,
) {
    fun toEntity(): UserTask {
        return UserTask(
            userId = userId,
            taskId = taskId,
            assignedAt = null,
            archivedAt = null
        )
    }
}
