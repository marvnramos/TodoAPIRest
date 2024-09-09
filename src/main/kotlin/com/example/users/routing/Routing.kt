package com.example.users.routing

import com.example.commons.dtos.ResDataDto
import com.example.commons.validation.HttpValidationHelper
import com.example.users.commands.CreateUserCommand
import com.example.users.domain.models.User
import com.example.users.dtos.requests.AddRequestDto
import com.example.users.dtos.responses.UserResponseDto
import com.example.users.middlewares.UserMiddleware
import com.example.users.middlewares.UserMiddleware.Companion.validateUser
import com.example.users.repositories.implementation.UserRepository
import com.example.users.services.implementations.UserServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.mindrot.jbcrypt.BCrypt
import java.time.Instant

fun Application.configureUsersRoutes() {
    val userRepository = UserRepository()
    val userService = UserServiceImpl(userRepository)
    val userMiddleware = UserMiddleware(userService)

    routing {
        route("/api/v1") {
            post("/users/store") {
                try {
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
                        username = user.username,
                        email = user.email,
                        profilePhoto = user.profilePhoto,
                        password = user.password,
                    )
                    userService.createUser(command)

                    call.respond(
                        HttpStatusCode.OK,
                        UserResponseDto("success", "User was created", ResDataDto.Single(user))
                    )

                } catch (e: IllegalArgumentException) {
                    HttpValidationHelper.responseError(call, e.message ?: "Invalid data")
                } catch (e: NullPointerException) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        "A null value was encountered \n error: ${e.message}"
                    )
                } catch (err: Exception) {
                    println("Something went wrong: $err")
                    call.respond(HttpStatusCode.InternalServerError, "An error occurred")
                }
            }
        }
    }
}
