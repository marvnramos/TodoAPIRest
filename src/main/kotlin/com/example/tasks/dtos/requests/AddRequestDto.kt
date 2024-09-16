package com.example.tasks.dtos.requests

import com.example.commons.serializers.InstantSerializer
import com.example.tasks.domain.Priority
import com.example.tasks.domain.Status
import com.example.tasks.dtos.requests.interfaces.IAddRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class AddRequestDto(
    override val title: String,
    override val description: String?,
    @Serializable(with = InstantSerializer::class)
    @SerialName("due_date") override val dueDate: Instant?,
    override val status: Status?,
    override val priority: Priority?,
    val sharedWith: List<String>? = null
) : IAddRequest {
    fun toAddSharedWithDto(): AddSharedWithDto {
        return AddSharedWithDto(
            title = title,
            description = description,
            dueDate = dueDate,
            status = status,
            priority = priority,
            sharedWith = sharedWith
        )
    }
}