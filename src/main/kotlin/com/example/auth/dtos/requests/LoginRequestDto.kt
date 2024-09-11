package com.example.auth.dtos.requests

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val username: String,
    val password: String
)
