package com.example.relationships.entities

import com.example.users.entities.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp


object Relationships : Table() {
    private val userId = uuid("user_id").references(Users.id)
    private val friendId = uuid("friend_id").references(Users.id)
    val createdAt = timestamp("created_at")
    val deletedAt = timestamp("deleted_at")

    override val primaryKey = PrimaryKey(userId, friendId)
}