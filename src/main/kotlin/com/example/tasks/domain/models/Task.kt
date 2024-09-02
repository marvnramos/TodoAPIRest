package com.example.tasks.domain.models

import com.example.commons.models.Entity
import com.example.tasks.domain.Status
import java.time.Instant
import java.util.*

class Task(
    override var id: UUID? = UUID.randomUUID(),
    val title: String,
    val description: String,
    val status: Status,
    val dueDate: Instant,
    val createdBy: UUID? = null,
    val createdAt: Instant,
    val updatedAt: Instant
) : Entity(id)