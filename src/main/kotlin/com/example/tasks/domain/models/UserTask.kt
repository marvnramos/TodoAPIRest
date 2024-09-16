package com.example.tasks.domain.models

import com.example.commons.serializers.InstantSerializer
import com.example.commons.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
data class UserTask(
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val taskId: UUID,
    @Serializable(with = InstantSerializer::class)
    val assignedAt: Instant?,
    @Serializable(with = InstantSerializer::class)
    val archivedAt: Instant?,
)