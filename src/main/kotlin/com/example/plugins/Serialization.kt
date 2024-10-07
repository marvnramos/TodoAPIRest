package com.example.plugins

import com.example.tasks.domain.models.Task
import com.example.auth.domain.models.JWT
import com.example.users.domain.models.User
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass


val modules = SerializersModule {
    polymorphic(Any::class) {
        subclass(Task::class)
        subclass(User::class)
        subclass(JWT::class)

    }
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(
            Json {
                serializersModule = modules
                prettyPrint = true
                isLenient = true
            }
        )
    }
}
