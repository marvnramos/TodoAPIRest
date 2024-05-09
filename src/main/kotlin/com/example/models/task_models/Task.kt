package com.example.models.task_models

import com.example.models.task_models.enums.Status
import com.example.models.users_models.Users
import org.jetbrains.exposed.sql.*
import java.util.*

data class Task (
    val id: UUID,
    val title: String,
    val description: String,
    val status: Status,
    val icon: String,
    val dueDate: Date,
    val userId: UUID,
    val createdAt:  Date,
    val updatedAt: Date
)

object Tasks : Table() {
    private  val id = uuid("id")
    val title = varchar("title", 25)
    val description = varchar("description", 255)
    val status = enumeration<Status>("status")
    val icon = varchar("icon", 50)
    val dueDate = long("due_date")
    val userId = uuid("user_id").references(Users.id)
    val createdAt = long("created_at")
    val updatedAt = long("updated_at")

    override val primaryKey = PrimaryKey(id)
}

