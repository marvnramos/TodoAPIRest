package com.example.models.users_models

import java.util.*
import org.jetbrains.exposed.sql.*

data class User(
    val id: UUID,
    val username: String,
    val password: String,
    val createdAt: Date,
    val updatedAt: Date
)

object Users: Table(){
    val id = uuid("id")
    val username = varchar("username", 255)
    val password = varchar("password", 255)
    val createdAt = long("created_at")
    val updatedAt = long("updated_at")

    override val primaryKey = PrimaryKey(id)
}