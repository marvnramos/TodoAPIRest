package com.example.tasks.domain.models

import com.example.commons.serializers.InstantSerializer
import com.example.commons.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.*

@Serializable
data class SharedTask(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val title: String,
    val description: String?,
    val statusId: Int?, // Here would go the auth user status
    val priorityId: Int?, // Everyone has the same priority and can be changed by the owner
    @Serializable(with = InstantSerializer::class)
    val dueDate: Instant?,
    @Serializable(with = UUIDSerializer::class)
    val createdBy: UUID,
    val sharedWith: List<@Serializable(with = UUIDSerializer::class) UUID>,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant,
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant,
)