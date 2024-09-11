package com.example.users.dtos.requests

import kotlinx.serialization.Serializable

@Serializable
data class SendEmailRequestDto(
    val email: String
)