package com.example.tasks.dtos.responses

import ResDataSerializer
import com.example.commons.dtos.IResponseDto
import com.example.commons.dtos.ResDataDto
import com.example.tasks.domain.models.SharedTask
import kotlinx.serialization.Serializable

@Serializable
data class SharedTasksResponseDto(
    override val status: String,
    override val message: String,
    @Serializable(with = ResDataSerializer::class)
    override val data: ResDataDto<SharedTask>
) : IResponseDto<ResDataDto<SharedTask>>
