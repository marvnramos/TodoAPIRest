package com.example.tasks.domain.entities

import com.example.tasks.domain.Status
import java.util.*

class PartialTask(
    override var id: UUID = UUID.randomUUID(),
    override val title: String,
    override val description: String,
    override val dueDate: String,
    override val status: Status,
    override val createdBy: UUID
) : Task(id, title, description, dueDate, status, createdBy)