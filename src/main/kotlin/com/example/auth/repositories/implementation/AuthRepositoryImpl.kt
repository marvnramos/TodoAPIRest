package com.example.auth.repositories.implementation

import com.example.auth.repositories.AuthRepository
import io.ktor.server.config.*

open class AuthRepositoryImpl(appConfig: ApplicationConfig) : AuthRepository(appConfig)