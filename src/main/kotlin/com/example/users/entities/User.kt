package com.example.users.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object Users : Table() {
    val id = uuid("id")
    val username = varchar("username", 50)
    val email = varchar("email", 100)
    val profilePhoto = varchar("profile_photo", 255)
    val password = varchar("password", 12)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")

    override val primaryKey = PrimaryKey(id)
}
