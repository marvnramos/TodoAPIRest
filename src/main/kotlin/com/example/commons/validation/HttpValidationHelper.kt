package com.example.commons.validation

import com.example.commons.validation.dtos.ValidationResponseDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

object HttpValidationHelper {
    suspend fun responseError(call: ApplicationCall, message: String) {
        val response = ValidationResponseDto(message)
        call.respond(HttpStatusCode.BadRequest, response)
    }
}