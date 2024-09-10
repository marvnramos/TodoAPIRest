package com.example.users.routing

import com.example.commons.dtos.ResDataDto
import com.example.commons.validation.HttpValidationHelper
import com.example.users.commands.CreateUserCommand
import com.example.users.commands.GetByUsernameCommand
import com.example.users.domain.models.JWT
import com.example.users.domain.models.User
import com.example.users.dtos.requests.AddRequestDto
import com.example.users.dtos.requests.LoginRequestDto
import com.example.users.dtos.requests.SendEmailRequestDto
import com.example.users.dtos.responses.LoginResponse
import com.example.users.dtos.responses.UserResponseDto
import com.example.users.middlewares.UserMiddleware
import com.example.users.middlewares.UserMiddleware.Companion.validateUser
import com.example.users.middlewares.UserMiddleware.Companion.validateUserLogin

import com.example.users.repositories.implementation.UserRepository
import com.example.users.services.implementations.UserServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import org.mindrot.jbcrypt.BCrypt
import java.time.Instant
import java.util.*
import com.auth0.jwt.algorithms.Algorithm


import  com.auth0.jwt.JWT as jwt

fun Application.configureUsersRoutes(args: Array<String>) {
    val userRepository = UserRepository()
    val userService = UserServiceImpl(userRepository)
    val userMiddleware = UserMiddleware(userService)

    val env = commandLineEnvironment(args)
    val appConfig = env.config

    val audience = appConfig.property("ktor.deployment.jwtAudience").getString()
    val secret = appConfig.property("ktor.deployment.jwtSecret").getString()
    val jwtDomain = appConfig.property("ktor.deployment.jwtDomain").getString()

    routing {
        route("/api/v1/users") {
            post("/auth/login") {
                try {
                    val request = call.receive<LoginRequestDto>()

                    validateUserLogin(request, userMiddleware)

                    val command = GetByUsernameCommand(request.username)
                    val user = userService.getUserByUsername(command)

                    if (!BCrypt.checkpw(request.password, user?.password)) {
                        throw IllegalArgumentException("Invalid credentials")
                    }

                    val token = jwt.create()
                        .withSubject("Authentication")
                        .withAudience(audience)
                        .withIssuer(jwtDomain)
                        .withClaim("sub", user?.id.toString())
                        .withExpiresAt(Date(System.currentTimeMillis() + 3_600_000))
                        .sign(Algorithm.HMAC256(secret))

                    val refreshToken = jwt.create()
                        .withSubject("Refresh")
                        .withIssuer(jwtDomain)
                        .withClaim("sub", user?.id.toString())
                        .withExpiresAt(Date(System.currentTimeMillis() + (2.52e+7).toLong()))
                        .sign(Algorithm.HMAC256(secret))

                    val tokens = JWT(
                        accessToken = token,
                        refreshToken = refreshToken
                    )

                    call.respond(
                        HttpStatusCode.OK,
                        LoginResponse("success", "you're logged now", ResDataDto.Single(tokens))
                    )

                } catch (e: IllegalArgumentException) {
                    HttpValidationHelper.responseError(call, e.message ?: "Invalid data")
                } catch (e: BadRequestException) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid request payload")
                } catch (err: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "An unexpected error occurred")
                }
            }

            post("/store") {
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

                } catch (e: BadRequestException) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid request payload")
                } catch (e: IllegalArgumentException) {
                    HttpValidationHelper.responseError(call, e.message ?: "Invalid data")
                } catch (e: NullPointerException) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        "A null value was encountered"
                    )
                } catch (err: Exception) {
                    println("Something went wrong: $err")
                    call.respond(HttpStatusCode.InternalServerError, "An error occurred")
                }
            }
            post("/request-password-change") {
                try {
                    val request = call.receive<SendEmailRequestDto>()
                    userMiddleware.validateEmail(request.email)

                    val props = Properties().apply {
                        put("mail.smtp.auth", "true")
                        put("mail.smtp.starttls.enable", "true")
                        put("mail.smtp.host", "smtp.gmail.com")
                        put("mail.smtp.port", "587")
                    }

                    val session = Session.getInstance(props, object : Authenticator() {
                        override fun getPasswordAuthentication(): PasswordAuthentication {
                            return PasswordAuthentication(
                                appConfig.property("ktor.deployment.email_credentials.mail").getString(),
                                appConfig.property("ktor.deployment.email_credentials.password").getString()
                            )
                        }
                    })

                    val message = MimeMessage(session).apply {
                        setFrom(InternetAddress("example@gmail.com"))
                        setRecipients(Message.RecipientType.TO, InternetAddress.parse(request.email))
                        subject = "Cambio de contraseña"
                        setText("Instrucciones para cambiar la contraseña")
                    }

                    Transport.send(message)
                    println("Correo enviado exitosamente a ${request.email}")
                    call.respond(HttpStatusCode.OK, "Correo enviado exitosamente")

                } catch (e: AuthenticationFailedException) {
                    println("Error de autenticación: ${e.message}")
                    call.respond(HttpStatusCode.Unauthorized, "Error de autenticación. Verifica tus credenciales.")
                } catch (e: MessagingException) {
                    println("Error al enviar el correo: ${e.message}")
                    call.respond(HttpStatusCode.InternalServerError, "Error al enviar el correo.")
                } catch (e: BadRequestException) {
                    println("Something went wrong: ${e.message}")
                    call.respond(HttpStatusCode.BadRequest, "Invalid request")
                } catch (e: IllegalArgumentException) {
                    HttpValidationHelper.responseError(call, e.message ?: "Invalid data")
                }
            }
            patch("/{id}/forgot-password") {

            }
        }
    }
}
