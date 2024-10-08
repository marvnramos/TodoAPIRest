package com.example.users.routing


import com.example.commons.validation.HttpValidationHelper
import com.example.users.commands.HandleUserCommand
import com.example.users.dtos.requests.SendEmailRequestDto
import com.example.users.middlewares.UserMiddleware
import com.example.users.repositories.implementation.UserRepository
import com.example.users.routing.handlers.singUpHandler
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
import java.util.*

fun Application.configureUsersRoutes(args: Array<String>) {
    val userRepository = UserRepository()
    val userService = UserServiceImpl(userRepository)
    val userMiddleware = UserMiddleware(userService)

    val env = commandLineEnvironment(args)
    val appConfig = env.config

    routing {
        route("/api/v1/users") {
            post("/store") {
                singUpHandler(
                    HandleUserCommand(
                        call = call,
                        userService = userService,
                        userMiddleware = userMiddleware
                    )
                )
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
                // TODO: Forgot password
                call.respond(HttpStatusCode.NotImplemented, "Forgot password endpoint not implemented yet.")

            }
            patch("/update-info/{id}") {
                // TODO: Update user info
                call.respond(HttpStatusCode.NotImplemented, "Update user endpoint not implemented yet.")
            }
            get("/{id}") {
                // TODO: Get user by id
                call.respond(HttpStatusCode.NotImplemented, "Get user info by ID endpoint not implemented yet.")
            }
        }
    }
}
