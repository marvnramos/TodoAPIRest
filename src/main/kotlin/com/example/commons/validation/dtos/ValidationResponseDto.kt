package com.example.commons.validation.dtos

import com.example.commons.validation.interfaces.IValidationResponse
import kotlinx.serialization.Serializable

@Serializable
data class ValidationResponseDto(
    override val message: String,
) : IValidationResponse