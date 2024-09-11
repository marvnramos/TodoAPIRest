package com.example.auth.dtos.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequestDto(
    @SerialName("refresh_token")
    val refreshToken: String
)