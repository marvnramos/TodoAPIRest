package com.example.tasks.domain.entities

import com.example.commons.entities.Entity
import com.example.tasks.domain.Status
import java.util.*

class Task(
    override var id: UUID = UUID.randomUUID(),

    val title: String,
    val description: String,
    val dueDate: String,
    val status: Status,
    val createdBy: UUID,
) : Entity(id)