package com.example.tasks.routing.handlers

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.*

fun getAuthUserId(call: ApplicationCall): UUID {
    val principal = call.principal<JWTPrincipal>()
    return UUID.fromString(principal?.payload?.getClaim("sub")?.asString())
}