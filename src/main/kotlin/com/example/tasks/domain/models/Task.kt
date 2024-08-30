package com.example.tasks.domain.models

import com.example.commons.models.Entity
import com.example.tasks.domain.Status
import java.time.LocalDate
import java.util.*

class Task(
    override var id: UUID? = UUID.randomUUID(),
    val title: String,
    val description: String,
    val dueDate: LocalDate,
    val status: Status,
    val createdBy: UUID? = null,
) : Entity(id)