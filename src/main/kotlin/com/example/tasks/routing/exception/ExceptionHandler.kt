package com.example.tasks.routing.exception

import com.example.commons.dtos.ResDataDto
import com.example.commons.validation.HttpValidationHelper
import com.example.tasks.dtos.responses.TaskResponseDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*

suspend fun exceptionHandler(
    call: ApplicationCall,
    execute: suspend () -> Unit
) {
    try {
        execute()
    } catch (e: BadRequestException) {
        HttpValidationHelper.responseError(call, "Invalid request payload")
    } catch (e: Exception) {
        when (e) {
            is IllegalArgumentException -> HttpValidationHelper.responseError(call, e.message ?: "Invalid data")
            is TaskException -> call.respond(
                HttpStatusCode.InternalServerError, TaskResponseDto(
                    status = "error",
                    message = e.message ?: "An error occurred",
                    data = ResDataDto.Multiple(emptyList())
                )
            )

            is CannotTransformContentToTypeException -> HttpValidationHelper.responseError(
                call,
                "Request payload required!"
            )

            else -> {
                println(e)
                call.respond(
                    HttpStatusCode.InternalServerError, TaskResponseDto(
                        status = "error",
                        message = "An error occurred",
                        data = ResDataDto.Multiple(emptyList())
                    )
                )
            }
        }
    }
}