package com.example.entities

import org.jetbrains.exposed.sql.Table

object Users: Table(){
    val id = uuid("id")
    val username = varchar("username", 255)
    val password = varchar("password", 255)
    val createdAt = long("created_at")
    val updatedAt = long("updated_at")

    override val primaryKey = PrimaryKey(id)
}