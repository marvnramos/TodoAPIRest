package com.example.tasks.domain.entities

import com.example.commons.entities.Entity
import com.example.tasks.domain.Status
import java.util.*

abstract class Task(
    override var id : UUID = UUID.randomUUID(),

    open val title: String,
    open val description: String,
    open val dueDate: String,
    open val status: Status,
    open val createdBy: UUID
): Entity(id)