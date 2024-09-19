package com.example.tasks.dtos.requests.interfaces

import com.example.commons.serializers.InstantSerializer
import com.example.tasks.domain.Priority
import com.example.tasks.domain.Status
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

interface IAddRequest {
    val title: String
    val description: String?

    @Serializable(with = InstantSerializer::class)
    @SerialName("due_date")
    val dueDate: Instant?

    @SerialName("status_id")
    val statusId: Int?

    @SerialName("priority_id")
    val priorityId: Int?
}