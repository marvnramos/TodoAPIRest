package com.example.tasks.domain.entities

import com.example.commons.serializers.DateSerializer
import com.example.commons.serializers.UUIDSerializer
import com.example.tasks.domain.Status
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class PartialTask(
    val title: String? = null,
    val description: String? = null,
    @Serializable(with = DateSerializer::class)
    val dueDate: String? = null,
    val status: Status,
    @Serializable(with = UUIDSerializer::class)
    val createdBy: UUID? = null,
)