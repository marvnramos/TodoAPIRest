package com.example.libs

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*

fun Application.configureSecurity(args: Array<String>) {
    // Please read the jwt property from the config file if you are using EngineMain

    val env = commandLineEnvironment(args)
    val appConfig = env.config

    val jwtAudience = appConfig.property("ktor.deployment.jwtAudience").getString()
    val jwtDomain = appConfig.property("ktor.deployment.jwtDomain").getString()
    val jwtRealm = appConfig.property("ktor.deployment.jwtRealm").getString()
    val jwtSecret = appConfig.property("ktor.deployment.jwtSecret").getString()

    authentication {
        jwt {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain) // todo: to set a valid issuer here
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}