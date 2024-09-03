package com.example.tasks.commands

import com.example.tasks.domain.Status
import com.example.tasks.domain.models.Task
import java.time.Instant
import java.util.*

data class CreateTaskCommand(
    val title: String,
    val description: String,
    val dueDate: Instant,
    val status: Status,
    val createdBy: UUID,
){
    fun toEntity(): Task {
        return Task(
            title = title,
            description = description,
            dueDate = dueDate,
            status = status,
            createdBy = createdBy,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )
    }
}