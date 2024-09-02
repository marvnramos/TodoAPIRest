package com.example.tasks.dtos.responses

import com.example.commons.interfaces.IResponseDto
import com.example.commons.models.ResData
import com.example.tasks.domain.models.Task
import kotlinx.serialization.Serializable

@Serializable
data class TaskResponseDto(
    override val status: String,
    override val message: String,
    override val data: ResData<Task>
) : IResponseDto<ResData<Task>>
