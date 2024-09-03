package com.example

import com.example.plugins.DatabaseSingleton
import com.example.plugins.configureHTTP
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    val env = commandLineEnvironment(args)
    val appConfig = env.config

    val host = appConfig.property("ktor.deployment.host").getString()
    val port = appConfig.property("ktor.deployment.port").getString().toInt()
6

    embeddedServer(Netty, port = port, host = host){
        module(args)
    }
        .start(wait = true)
}

fun Application.module(args: Array<String>) {
    val configureDatabase = DatabaseSingleton(args)

    configureSerialization()
    configureDatabase.init()
    configureHTTP()
    configureSecurity(args)
    configureRouting()
}
