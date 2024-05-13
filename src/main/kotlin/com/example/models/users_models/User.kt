package com.example.models.users_models

import com.example.utils.DateSerializer
import com.example.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*
import org.jetbrains.exposed.sql.*

@Serializable
data class User(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = UUID.randomUUID(),
    val username: String,
    val password: String,
    @Serializable(with = DateSerializer::class)
    var createdAt: Date? = Date(),
    @Serializable(with = DateSerializer::class)
    var updatedAt: Date? = Date()
)

object Users: Table(){
    val id = uuid("id")
    val username = varchar("username", 255)
    val password = varchar("password", 255)
    val createdAt = long("created_at")
    val updatedAt = long("updated_at")

    override val primaryKey = PrimaryKey(id)
}