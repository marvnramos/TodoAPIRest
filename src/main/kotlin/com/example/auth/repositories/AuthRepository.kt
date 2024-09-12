package com.example.auth.repositories

import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.JWT as jwt
import com.example.auth.repositories.interfaces.IAuthRepository
import com.example.users.commands.GetByUsernameCommand
import com.example.users.repositories.implementation.UserRepository
import com.example.users.services.implementations.UserServiceImpl
import io.ktor.server.config.*
import org.mindrot.jbcrypt.BCrypt

import java.util.*

abstract class AuthRepository(appConfig: ApplicationConfig) : IAuthRepository {
    private val userRepository = UserRepository()
    private val userService = UserServiceImpl(userRepository)

    private val audience = appConfig.property("ktor.deployment.jwtAudience").getString()
    private val secret = appConfig.property("ktor.deployment.jwtSecret").getString()
    private val jwtDomain = appConfig.property("ktor.deployment.jwtDomain").getString()
    private val jwtAccessExpiration = appConfig.property("ktor.deployment.jwt.expiredAtAccess").getString().toLong()
    private val jwtRefreshExpiration = appConfig.property("ktor.deployment.jwt.expiredAtRefresh").getString().toLong()


    override suspend fun refreshToken(username: String): String {
        val command = GetByUsernameCommand(username)
        val user = userService.getUserByUsername(command)

        return jwt.create()
            .withSubject("Refresh")
            .withIssuer(jwtDomain)
            .withClaim("sub", user?.id.toString())
            .withClaim("username", user?.username)
            .withExpiresAt(Date(System.currentTimeMillis() + jwtRefreshExpiration))
            .sign(Algorithm.HMAC256(secret))
    }

    override suspend fun accessToken(username: String): String {
        val command = GetByUsernameCommand(username)
        val user = userService.getUserByUsername(command)

        return jwt.create()
            .withSubject("Authentication")
            .withAudience(audience)
            .withIssuer(jwtDomain)
            .withClaim("sub", user?.id.toString())
            .withClaim("username", user?.username)
            .withExpiresAt(Date(System.currentTimeMillis() + jwtAccessExpiration))
            .sign(Algorithm.HMAC256(secret))
    }

    override suspend fun validateToken(token: String): DecodedJWT {
        try {
            return jwt.require(Algorithm.HMAC256(secret))
                .withIssuer(jwtDomain)
                .build()
                .verify(token)
        } catch (e: JWTVerificationException) {
            throw JWTVerificationException("Invalid or expired refresh token")
        }
    }

    override suspend fun checkPassword(username: String, password: String) {
        try {
            val command = GetByUsernameCommand(username)
            val user = userService.getUserByUsername(command) ?: throw IllegalArgumentException("Invalid credentials")

            if (!BCrypt.checkpw(password, user.password)) throw IllegalArgumentException("Invalid credentials")
        } catch (e: Exception) {
            throw Exception("something went wrong")
        }
    }
}