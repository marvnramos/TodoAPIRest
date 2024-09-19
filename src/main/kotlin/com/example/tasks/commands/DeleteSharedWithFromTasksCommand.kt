package com.example.tasks.commands

import com.example.tasks.domain.models.UserTask
import java.util.*

data class DeleteSharedWithFromTasksCommand(
    val userId: UUID,
    val taskId: UUID,
    val toDeleteUserId: UUID
){
    fun toEntity(): UserTask {
        return UserTask(
            userId = userId,
            taskId = taskId,
            assignedAt = null,
            archivedAt = null
        )
    }
}