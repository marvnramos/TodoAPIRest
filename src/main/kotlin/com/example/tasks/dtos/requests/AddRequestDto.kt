package com.example.tasks.dtos.requests

import com.example.commons.serializers.InstantSerializer
import com.example.commons.serializers.UUIDSerializer
import com.example.tasks.domain.Status
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
data class AddRequestDto(
    val title: String,
    val description: String,
    @Serializable(with = InstantSerializer::class)
    val dueDate: Instant,
    val status: Status? = Status.TODO,
    @Serializable(with = UUIDSerializer::class)
    val createdBy: UUID,
)