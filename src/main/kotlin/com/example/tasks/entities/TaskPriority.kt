package com.example.tasks.entities

import com.example.tasks.domain.Priority
import org.jetbrains.exposed.sql.Table

object TaskPriority : Table("task_priorities") {
    val id = integer("id").autoIncrement()
    val priority = enumerationByName("priority", 10, Priority::class).default(Priority.LOW).uniqueIndex()

    override val primaryKey = PrimaryKey(id)
}