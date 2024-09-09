package com.example.users.dtos.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordRequestDto(
    @SerialName("new_password")
    val newPassword: String,
)
