package com.example.users.dtos.responses

import ResDataSerializer
import com.example.commons.dtos.IResponseDto
import com.example.commons.dtos.ResDataDto
import com.example.users.domain.models.User
import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDto(
    override val status: String,
    override val message: String,
    @Serializable(with = ResDataSerializer::class)
    override val data: ResDataDto<User>,
) : IResponseDto<ResDataDto<User>>