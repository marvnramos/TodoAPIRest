package com.example.libs

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.response.*

fun Application.configureSecurity(args: Array<String>) {

    val env = commandLineEnvironment(args)
    val appConfig = env.config

    val jwtAudience = appConfig.property("ktor.deployment.jwtAudience").getString()
    val jwtDomain = appConfig.property("ktor.deployment.jwtDomain").getString()
    val jwtRealm = appConfig.property("ktor.deployment.jwtRealm").getString()
    val jwtSecret = appConfig.property("ktor.deployment.jwtSecret").getString()

    install(Authentication){
        jwt ("auth-jwt"){
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience) && credential.payload.getClaim("username").asString() != "") JWTPrincipal(credential.payload) else null
            }
            challenge{ defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}