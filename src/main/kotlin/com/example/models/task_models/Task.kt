package com.example.models.task_models

import com.example.models.task_models.enums.Status
import com.example.utils.DateSerializer
import com.example.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Task (
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = UUID.randomUUID(),
    val title: String,
    val description: String,
    val status: Status,
    val icon: String,
    @Serializable(with = DateSerializer::class)
    val dueDate: Date,
    @Serializable(with = UUIDSerializer::class)
    var userId: UUID? = null,
    @Serializable(with = DateSerializer::class)
    val createdAt:  Date? = Date(),
    @Serializable(with = DateSerializer::class)
    val updatedAt: Date? = Date()
)

