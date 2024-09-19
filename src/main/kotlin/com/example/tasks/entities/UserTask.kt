package com.example.tasks.entities

import com.example.users.entities.Users
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.Table

object UserTasks : Table() {
    val userId = uuid("user_id").references(Users.id)
    val taskId = uuid("task_id").references(Tasks.id)
    val assignedAt = timestamp("assigned_at")
    val archivedAt = timestamp("archived_at").nullable()

    override val primaryKey = PrimaryKey(userId, taskId)
}