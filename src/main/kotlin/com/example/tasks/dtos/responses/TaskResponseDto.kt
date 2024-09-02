package com.example.tasks.dtos.responses

import com.example.commons.interfaces.IResponseDto
import com.example.tasks.domain.models.Task
import com.example.tasks.dtos.TaskDataDto
import kotlinx.serialization.Serializable

@Serializable
data class TaskResponseDto<T>(
    override val status: String,
    override val message: String,
    override val data: T,
) : IResponseDto<T>