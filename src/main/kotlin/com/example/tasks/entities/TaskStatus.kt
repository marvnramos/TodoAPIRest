package com.example.tasks.entities

import com.example.tasks.domain.Status
import org.jetbrains.exposed.sql.Table

object TaskStatus : Table("task_status") {
    val id = integer("id").autoIncrement()
    val status = enumerationByName("status", 10, Status::class).default(Status.TODO).uniqueIndex()

    override val primaryKey = PrimaryKey(id)
}