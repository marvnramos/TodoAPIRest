package com.example.users.routing.handlers

import com.example.commons.dtos.ResDataDto
import com.example.users.dtos.requests.AddRequestDto
import com.example.users.commands.CreateUserCommand
import com.example.users.commands.HandleUserCommand
import com.example.users.domain.models.User
import com.example.users.dtos.responses.UserResponseDto
import com.example.users.middlewares.UserMiddleware.Companion.validateUser
import com.example.users.routing.exception.UserExceptionHandler
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.mindrot.jbcrypt.BCrypt
import java.time.Instant

suspend fun singUpHandler(
    handleUserCommand: HandleUserCommand,
    userException: UserExceptionHandler = UserExceptionHandler()
) {
    val (call, userService, userMiddleware) = handleUserCommand

    userException.exceptionHandler(call) {
        val request = call.receive<AddRequestDto>()
        validateUser(request, userMiddleware)

        val hashPassword = BCrypt.hashpw(request.password, BCrypt.gensalt())
        val user = User(
            username = request.username!!,
            email = request.email!!,
            profilePhoto = request.profilePhoto ?: "https://example.com/photo.jpg",
            password = hashPassword,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )

        val command = CreateUserCommand(
            id = user.id,
            username = user.username,
            email = user.email,
            profilePhoto = user.profilePhoto,
            password = user.password,
        )
        userService.createUser(command)

        call.respond(
            HttpStatusCode.Created,
            UserResponseDto("success", "User was created", ResDataDto.Single(user))
        )
    }
}