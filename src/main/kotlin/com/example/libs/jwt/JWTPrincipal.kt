package com.example.libs.jwt

import com.auth0.jwt.interfaces.Payload
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

class JWTPrincipal (payload: Payload) : JWTPayloadHolder(payload), Principal