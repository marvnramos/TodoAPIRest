package com.example.auth.services.interfaces

import com.auth0.jwt.interfaces.DecodedJWT
import com.example.auth.commands.CreateAccessTokenCommand
import com.example.auth.commands.CreateRefreshTokenCommand
import com.example.auth.commands.PasswordCheckCommand
import com.example.auth.commands.TokenValidationCommand

interface IAuthService {
    suspend fun generateToken(command: CreateAccessTokenCommand): String
    suspend fun generateRefreshToken(command: CreateRefreshTokenCommand): String
    suspend fun checkUserPassword(command: PasswordCheckCommand): Boolean
    suspend fun validateRefreshToken(command: TokenValidationCommand): DecodedJWT
}