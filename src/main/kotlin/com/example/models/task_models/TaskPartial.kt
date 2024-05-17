package com.example.models.task_models

import com.example.models.task_models.enums.Status
import com.example.utils.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TaskPartial (
    val title: String?= null,
    val description: String?= null,
    val status: Status? = null,
    val icon: String? = null,
    @Serializable(with = DateSerializer::class)
    val dueDate: Date? = null,
    @Serializable(with = DateSerializer::class)
    val updatedAt: Date? = Date()
)