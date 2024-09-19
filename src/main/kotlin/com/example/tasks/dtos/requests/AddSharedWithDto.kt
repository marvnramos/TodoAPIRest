package com.example.tasks.dtos.requests

import com.example.commons.serializers.InstantSerializer
import com.example.commons.serializers.UUIDSerializer
import com.example.tasks.domain.Priority
import com.example.tasks.domain.Status
import com.example.tasks.dtos.requests.interfaces.IAddRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
data class AddSharedWithDto(
    override val title: String,
    override val description: String?,
    @Serializable(with = InstantSerializer::class)
    @SerialName("due_date") override val dueDate: Instant?,
    @SerialName("status_id") override val statusId: Int?,
    @SerialName("priority_id") override val priorityId: Int?,
    @SerialName("shared_with") val sharedWith: List<@Serializable(with = UUIDSerializer::class) UUID>?
) : IAddRequest
