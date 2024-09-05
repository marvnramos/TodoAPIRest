package com.example.tasks.dtos.responses

import ResDataSerializer
import com.example.commons.dtos.IResponseDto
import com.example.commons.dtos.ResDataDto
import com.example.tasks.domain.models.Task
import kotlinx.serialization.Serializable

@Serializable
data class TaskResponseDto(
    override val status: String,
    override val message: String,
    @Serializable(with = ResDataSerializer::class)
    override val data: ResDataDto<Task>
) : IResponseDto<ResDataDto<Task>>
