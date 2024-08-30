package com.example.tasks.commands

import com.example.tasks.domain.Status
import java.util.*

data class UpdateTaskCommand(
    val id: UUID,
    val title: String? = null,
    val description: String? = null,
    val dueDate: Date? = null,
    val status: Status,
)
