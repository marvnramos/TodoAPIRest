package com.example.notifications.entities

import com.example.notifications.domain.Type
import com.example.users.entities.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object Notifications : Table() {
    val id = uuid("id")
    val userId = uuid("user_id").references(Users.id)
    val type = enumerationByName("type", 20, Type::class)
    val message = text("message")
    val isRead = bool("is_read").default(false)
    val relatedId = uuid("related_id")
    val createdAt = timestamp("created_at")

    override val primaryKey = PrimaryKey(id)
}