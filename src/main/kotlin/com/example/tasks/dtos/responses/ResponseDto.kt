package com.example.tasks.dtos.responses

import kotlinx.serialization.Serializable

@Serializable
data class ResponseDto<T>(
    val status: String,
    val message: String,
    val data: T
)