package com.example.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.models.AuthModel
import com.example.models.users_models.User
import com.example.services.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.mindrot.jbcrypt.BCrypt
import java.util.*

fun Application.configureAuthRoutes(args: Array<String>){
    val userImpl = UserService()

    val env = commandLineEnvironment(args)
    val appConfig = env.config

    val audience = appConfig.property("ktor.deployment.jwtAudience").getString()
    val secret = appConfig.property("ktor.deployment.jwtSecret").getString()
//    val issuer = appConfig.property("ktor.deployment.jwtIssuer").toString()
    val jwtDomain = appConfig.property("ktor.deployment.jwtDomain").getString()


    routing {
        post("/v1/auth/login") {
            try {
                val payload = call.receive<User>()

                val user: User? = userImpl.getUser(payload.username)
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, "User not found!")
                    return@post
                }

                if (!BCrypt.checkpw(payload.password, user.password)) {
                    call.respond(HttpStatusCode.BadRequest, "Incorrect credentials!")
                    return@post
                }

                val userId = user.id.toString()

                val token = JWT.create()
                    .withAudience(audience)
                    .withIssuer(jwtDomain)
                    .withClaim("userId", userId)
                    .withExpiresAt(Date(System.currentTimeMillis() + 3_600_000))
                    .sign(Algorithm.HMAC256(secret))

                val response = AuthModel(token = token)

                call.respond(HttpStatusCode.OK, response)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error: ${e.message}")
            }
        }
    }
}