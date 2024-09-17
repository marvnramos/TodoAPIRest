package com.example.tasks.domain.models

import com.example.commons.models.Entity
import com.example.commons.serializers.InstantSerializer
import com.example.commons.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.*

@Serializable
data class Task(
    @Serializable(with = UUIDSerializer::class)
    override var id: UUID? = UUID.randomUUID(),
    val title: String,
    val description: String?,
    val statusId: Int?,
    val priorityId: Int?,
    @Serializable(with = InstantSerializer::class)
    val dueDate: Instant?,
    @Serializable(with = UUIDSerializer::class)
    val createdBy: UUID? = null,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant?,
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant?,
    val sharedWith: List<@Serializable(with = UUIDSerializer::class) UUID> = emptyList()
) : Entity(id)

