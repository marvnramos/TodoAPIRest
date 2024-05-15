package com.example.models.users_models

import com.example.utils.DateSerializer
import com.example.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class User(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = UUID.randomUUID(),
    val username: String,
    var password: String,
    @Serializable(with = DateSerializer::class)
    var createdAt: Date? = Date(),
    @Serializable(with = DateSerializer::class)
    var updatedAt: Date? = Date()
)