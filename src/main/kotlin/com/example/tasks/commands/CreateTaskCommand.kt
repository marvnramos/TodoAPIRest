package com.example.tasks.commands

import com.example.tasks.domain.Status
import java.util.*

data class CreateTaskCommand(
    val title: String,
    val description: String,
    val dueDate: String,
    val status: Status,
    val createdBy: UUID,
)
