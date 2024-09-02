package com.example.tasks.entities

import com.example.tasks.domain.Status
import com.example.xd.entities.Users // change this
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object Tasks : Table() {
    val id = uuid("id")
    val title = varchar("title", 25)
    val description = varchar("description", 255)
    val status = enumerationByName("status", 10, Status::class)
    val dueDate = timestamp("due_date")
    val createdBy = uuid("created_by").references(Users.id)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")

    override val primaryKey = PrimaryKey(id)
}
