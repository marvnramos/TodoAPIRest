package com.example.plugins

import com.example.tasks.domain.models.Task
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass


val taskModule = SerializersModule {
    polymorphic(Any::class){
        subclass(Task::class)
    }
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(
            Json {
                serializersModule = taskModule
                prettyPrint = true
                isLenient = true
            }
        )
    }
}
