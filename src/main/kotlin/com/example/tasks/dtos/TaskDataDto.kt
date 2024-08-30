package com.example.tasks.dtos

import com.example.commons.serializers.DateSerializer
import com.example.commons.serializers.UUIDSerializer
import com.example.tasks.domain.Status
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TaskDataDto(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val title: String,
    val description: String,
    @Serializable(with = DateSerializer::class)
    val dueDate: Date,
    val status: Status,
    val createdBy: String,
    @Serializable(with = DateSerializer::class)
    val createdAt: Date,
    @Serializable(with = DateSerializer::class)
    val archivedAt: Date? = null,
)
