package com.example.users.dtos.requests

import kotlinx.serialization.Serializable

@Serializable
data class ValidateTokenRequestDto(val token: String)
