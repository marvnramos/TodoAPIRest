package com.example.tasks.dtos.responses

import com.example.tasks.domain.Status
import java.util.*

data class TaskDataDto(
    val id: UUID,
    val title: String,
    val description: String,
    val dueDate: Date,
    val status: Status,
    val createdBy: UUID,
    val createdAt: Date,
    val archivedAt: Date? = null,
)
