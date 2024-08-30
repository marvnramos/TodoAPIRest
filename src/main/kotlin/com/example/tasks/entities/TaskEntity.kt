package com.example.tasks.entities

import com.example.tasks.domain.Status
import com.example.xd.entities.Users // change this
import org.jetbrains.exposed.sql.Table

object Tasks : Table() {
    val id = uuid("id")
    val title = varchar("title", 25)
    val description = varchar("description", 255)
    val status = enumeration<Status>("TODO")
    val icon = varchar("icon", 50)
    val dueDate = long("due_date")
    val userId = uuid("user_id").references(Users.id)
    val createdAt = long("created_at")
    val updatedAt = long("updated_at")

    override val primaryKey = PrimaryKey(id)
}
