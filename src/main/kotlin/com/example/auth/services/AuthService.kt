package com.example.auth.services

import com.auth0.jwt.interfaces.DecodedJWT
import com.example.auth.commands.CreateAccessTokenCommand
import com.example.auth.commands.CreateRefreshTokenCommand
import com.example.auth.commands.PasswordCheckCommand
import com.example.auth.commands.TokenValidationCommand
import com.example.auth.repositories.implementation.AuthRepositoryImpl
import com.example.auth.services.interfaces.IAuthService

abstract class AuthService(private val authRepository: AuthRepositoryImpl) : IAuthService {
    override suspend fun generateToken(command: CreateAccessTokenCommand): String {
        return authRepository.accessToken(command.username)
    }

    override suspend fun generateRefreshToken(command: CreateRefreshTokenCommand): String {
        return authRepository.refreshToken(command.username)
    }

    override suspend fun checkUserPassword(command: PasswordCheckCommand) {
        return authRepository.checkPassword(command.password, command.username)
    }

    override suspend fun validateRefreshToken(command: TokenValidationCommand): DecodedJWT {
        return authRepository.validateToken(command.token)
    }
}