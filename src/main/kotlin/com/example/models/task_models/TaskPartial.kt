package com.example.models.task_models

import com.example.models.task_models.enums.Status
import com.example.utils.DateSerializer
import com.example.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TaskPartial (
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = UUID.randomUUID(),
    val title: String?= null,
    val description: String?= null,
    val status: Status? = null,
    val icon: String? = null,
    @Serializable(with = DateSerializer::class)
    val dueDate: Date? = null,
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID? = null,
    @Serializable(with = DateSerializer::class)
    val createdAt:  Date? = Date(),
    @Serializable(with = DateSerializer::class)
    val updatedAt: Date? = Date()
)