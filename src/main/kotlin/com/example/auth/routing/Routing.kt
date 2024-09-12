package com.example.auth.routing

import com.auth0.jwt.exceptions.JWTVerificationException
import com.example.auth.commands.CreateAccessTokenCommand
import com.example.auth.commands.CreateRefreshTokenCommand
import com.example.auth.commands.PasswordCheckCommand
import com.example.auth.commands.TokenValidationCommand
import com.example.auth.dtos.requests.LoginRequestDto
import com.example.auth.dtos.requests.RefreshRequestDto
import com.example.auth.dtos.responses.AuthResponse
import com.example.auth.middlewares.AuthMiddleware
import com.example.auth.middlewares.AuthMiddleware.Companion.validateAuthentication
import com.example.auth.repositories.implementation.AuthRepositoryImpl
import com.example.auth.services.implementations.AuthServiceImpl
import com.example.commons.dtos.ResDataDto
import com.example.commons.validation.HttpValidationHelper
import com.example.users.repositories.implementation.UserRepository
import com.example.users.services.implementations.UserServiceImpl
import com.example.auth.domain.models.JWT as jwt
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureAuthRoutes(args: Array<String>) {
    val env = commandLineEnvironment(args)
    val appConfig = env.config

    val authRepo = AuthRepositoryImpl(appConfig)
    val authService = AuthServiceImpl(authRepo)

    val userRepository = UserRepository()
    val userService = UserServiceImpl(userRepository)

    val authMiddleware = AuthMiddleware(userService)

    routing {
        route("/api/v1/auth") {
            post("/login") {
                try {
                    val request = call.receive<LoginRequestDto>()
                    val (username, password) = request

                    validateAuthentication(request, authMiddleware)
                    val command = PasswordCheckCommand(username, password)
                    authService.checkUserPassword(command)


                    val tokenCommand = CreateAccessTokenCommand(username)
                    val token = authService.generateToken(tokenCommand)

                    val refresh = CreateRefreshTokenCommand(username)
                    val refreshToken = authService.generateRefreshToken(refresh)

                    val tokens = jwt(
                        accessToken = token,
                        refreshToken = refreshToken
                    )

                    call.respond(
                        HttpStatusCode.OK,
                        AuthResponse("success", "you're logged now", ResDataDto.Single(tokens))
                    )

                } catch (e: IllegalArgumentException) {
                    HttpValidationHelper.responseError(call, e.message ?: "Invalid data")
                } catch (e: BadRequestException) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid request payload")
                } catch (err: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, err.message ?: "An unexpected error occurred")
                }
            }
            post("/refresh") {
                try {
                    val request = call.receive<RefreshRequestDto>()
//                    TODO:
//                    validateRefresh(request)

                    val command = TokenValidationCommand(request.refreshToken)
                    val decodedJWT = authService.validateRefreshToken(command)
                    val username = decodedJWT.claims["username"]?.asString()

                    val accessTokenCommand = CreateAccessTokenCommand(username!!)
                    val newAccessToken = authService.generateToken(accessTokenCommand)


                    val refreshTokenCommand = CreateRefreshTokenCommand(username)
                    val newRefreshToken = authService.generateRefreshToken(refreshTokenCommand)

                    val tokens = jwt(
                        accessToken = newAccessToken,
                        refreshToken = newRefreshToken
                    )

                    call.respond(
                        HttpStatusCode.OK,
                        AuthResponse("success", "session refreshed", ResDataDto.Single(tokens))
                    )
                } catch (e: JWTVerificationException) {
                    call.respond(HttpStatusCode.Unauthorized, "${e.message}")
                } catch (e: IllegalArgumentException) {
                    HttpValidationHelper.responseError(call, e.message ?: "Invalid data")
                } catch (e: BadRequestException) {
                    HttpValidationHelper.responseError(call, e.message ?: "Invalid data")
                } catch (err: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "An error occurred")
                }
            }
        }
    }
}
