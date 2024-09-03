package com.example.tasks.entities

import com.example.users.entities.Users
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.Table

object UserTasks: Table(){
    private val userId = uuid("user_id").references(Users.id)
    private val taskId = uuid("task_id").references(Tasks.id)
    val assignedAt = timestamp("assigned_at")
    val archivedAt = timestamp("archived_at")

    override val primaryKey = PrimaryKey(userId, taskId)
}