package com.example.tasks.entities

import com.example.users.entities.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object Tasks : Table() {
    val id = uuid("id")
    val title = varchar("title", 25)
    val description = text("description").nullable()
    val statusId = integer("status_id").references(TaskStatus.id)
    val priorityId = integer("priority_id").references(TaskPriority.id)
    val dueDate = timestamp("due_date").nullable()
    val createdBy = uuid("created_by").references(Users.id)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")

    override val primaryKey = PrimaryKey(id)
}