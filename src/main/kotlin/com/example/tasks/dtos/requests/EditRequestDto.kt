package com.example.tasks.dtos.requests

import com.example.commons.serializers.InstantSerializer
import com.example.commons.serializers.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.*

@Serializable
data class EditRequestDto(
    val title: String?,
    val description: String?,
    @Serializable(with = InstantSerializer::class)
    @SerialName("due_date") val dueDate: Instant? = null,
    @SerialName("status_id") val statusId: Int?,
    @SerialName("priority_id") val priorityId: Int?,
    @SerialName("shared_with") val sharedWith: List<@Serializable(with = UUIDSerializer::class) UUID>? = emptyList()
)