package com.example.auth.dtos.responses

import ResDataSerializer
import com.example.commons.dtos.IResponseDto
import com.example.commons.dtos.ResDataDto
import com.example.auth.domain.models.JWT
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    override val status: String,
    override val message: String,
    @Serializable(with = ResDataSerializer::class)
    override val data: ResDataDto<JWT>,
) : IResponseDto<ResDataDto<JWT>>