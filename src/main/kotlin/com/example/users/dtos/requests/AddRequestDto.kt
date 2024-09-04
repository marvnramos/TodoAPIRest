package com.example.users.dtos.requests

import kotlinx.serialization.Serializable

@Serializable
data class AddRequestDto(
    val username: String,
    val email: String,
    val profilePhoto: String?,
    val password: String
)