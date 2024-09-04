package com.example.users.routing

import com.example.users.dtos.requests.AddRequestDto
import io.ktor.http.*
import io.ktor.http.cio.HttpMessage
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

data class res(
    val message: String
)

fun Application.configureUsersRoutes() {
    routing {
        route("/api/v1") {
            post("/users/store") {
                try {
                    val request = call.receive<AddRequestDto>()

                    if (request.username.isBlank()) {
                        call.respond(HttpStatusCode.BadRequest, res(
                            message = "Username is required"
                        ))
                        return@post
                    }

                    if (request.email.isBlank()) {
                        call.respond(HttpStatusCode.BadRequest, "Email is required")
                        return@post
                    }
                    if (request.email.isNotBlank()) {
                        val emailRegex = Regex("^((?!\\.)[\\w-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$")
                        if (!emailRegex.matches(request.email)) {
                            call.respond(HttpStatusCode.BadRequest, "Invalid email")
                            return@post
                        }
                    }

                    if (request.password.isBlank()) {
                        call.respond(HttpStatusCode.BadRequest, "Password is required")
                        return@post
                    }

                    if (request.password.isNotBlank()) {
                        val passwordRegex = Regex(
                            "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@#\$%^!&+=.\\-_]){2,}[A-Za-z0-9@#\$%^!&+=.\\-_]{8,}$"
                        )
                        if (!passwordRegex.matches(request.password)) {
                            call.respond(HttpStatusCode.BadRequest, "Invalid password form")
                            return@post
                        }
                    }

                    call.respond("You've logged! :3")


                    call.respond(HttpStatusCode.OK, request)
                } catch (err: Exception) {
                    println("Something went wrong: $err")
                    call.respond(HttpStatusCode.InternalServerError, "An error occurred")
                }
            }
        }
    }
}
