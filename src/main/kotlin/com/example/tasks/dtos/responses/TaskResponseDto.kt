package com.example.tasks.dtos.responses

import com.example.commons.interfaces.IResponseDto
import com.example.tasks.dtos.TaskDataDto
import kotlinx.serialization.Serializable

@Serializable
data class TaskResponseDto(
    override val message: String,
    override val status: String,
    override val data: TaskDataDto
): IResponseDto<TaskDataDto>