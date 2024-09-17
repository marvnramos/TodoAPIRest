package com.example.tasks.commands

import com.example.tasks.domain.models.UserTask
import java.time.Instant
import java.util.*

data class CreateSharedTaskCommand(
    val userId: UUID,
    val taskId: UUID,
) {
    fun toEntity(): UserTask {
        return UserTask(
            userId = userId,
            taskId = taskId,
            assignedAt = Instant.now(),
            archivedAt = null
        )
    }
}