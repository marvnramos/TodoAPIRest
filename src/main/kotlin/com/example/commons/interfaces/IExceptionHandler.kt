package com.example.commons.interfaces

import com.example.commons.dtos.ResDataDto
import com.example.commons.validation.HttpValidationHelper
import com.example.tasks.dtos.responses.TaskResponseDto
import com.example.tasks.routing.exception.TaskException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import jakarta.mail.AuthenticationFailedException
import jakarta.mail.MessagingException
import mu.KotlinLogging

abstract class IExceptionHandler {

    private val logger = KotlinLogging.logger {}

    companion object {
        const val INVALID_REQUEST = "Invalid request payload"
        const val AUTH_ERROR = "Authentication error. Please verify your credentials."
        const val EMAIL_ERROR = "Error sending the email."
        const val TRANSFORM_ERROR = "Request payload required!"
        const val GENERAL_ERROR = "An error occurred"
    }

    suspend fun exceptionHandler(call: ApplicationCall, execute: suspend () -> Unit) {
        try {
            execute()
        } catch (e: BadRequestException) {
            logger.warn { "BadRequestException: ${e.message}" }
            HttpValidationHelper.responseError(call, INVALID_REQUEST)
        } catch (e: IllegalArgumentException) {
            logger.warn { "IllegalArgumentException: ${e.message}" }
            HttpValidationHelper.responseError(call, e.message ?: INVALID_REQUEST)
        } catch (e: TaskException) {
            logger.error { "TaskException: ${e.message}" }
            call.respond(
                HttpStatusCode.InternalServerError,
                TaskResponseDto(
                    status = "error",
                    message = e.message ?: GENERAL_ERROR,
                    data = ResDataDto.Multiple(emptyList())
                )
            )
        } catch (e: AuthenticationFailedException) {
            logger.error { "AuthenticationFailedException: ${e.message}" }
            call.respond(HttpStatusCode.Unauthorized, AUTH_ERROR)
        } catch (e: MessagingException) {
            logger.error { "MessagingException: ${e.message}" }
            call.respond(HttpStatusCode.InternalServerError, EMAIL_ERROR)
        } catch (e: CannotTransformContentToTypeException) {
            logger.warn { "CannotTransformContentToTypeException: ${e.message}" }
            HttpValidationHelper.responseError(call, TRANSFORM_ERROR)
        } catch (e: NullPointerException) {
            logger.error { "NullPointerException: ${e.message}" }
            call.respond(
                HttpStatusCode.InternalServerError,
                "A null value was encountered"
            )
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            call.respond(
                HttpStatusCode.InternalServerError,
                TaskResponseDto(
                    status = "error",
                    message = GENERAL_ERROR,
                    data = ResDataDto.Multiple(emptyList())
                )
            )
        }
    }
}
