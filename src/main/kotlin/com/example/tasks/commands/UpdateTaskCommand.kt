package com.example.tasks.commands

import com.example.tasks.domain.Status
import com.example.tasks.domain.models.Task
import java.time.Instant
import java.time.LocalDate
import java.util.*

data class UpdateTaskCommand(
    val id: UUID,
    val title: String? = null,
    val description: String? = null,
    val dueDate: LocalDate? = null,
    val status: Status,
){
    fun updateEntity(task: Task): Task {
        return Task(
            title = task.title,
            description = task.description,
            dueDate = task.dueDate,
            status = task.status,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )
    }
}
