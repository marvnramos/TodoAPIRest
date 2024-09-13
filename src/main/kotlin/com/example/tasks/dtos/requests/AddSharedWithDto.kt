package com.example.tasks.dtos.requests

import com.example.commons.serializers.InstantSerializer
import com.example.tasks.domain.Priority
import com.example.tasks.domain.Status
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class AddSharedWithDto(
    val title: String,
    val description: String?,
    @Serializable(with = InstantSerializer::class)
    @SerialName("due_date") val dueDate: Instant?,
    val status: Status?,
    val priority: Priority?,
    val sharedWith: List<String>?
)
