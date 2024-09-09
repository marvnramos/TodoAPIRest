package com.example.users.dtos.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddRequestDto(
    val username: String? = null,
    val email: String? = null,
    @SerialName("profile_photo")
    val profilePhoto: String? = null,
    val password: String? = null,
)