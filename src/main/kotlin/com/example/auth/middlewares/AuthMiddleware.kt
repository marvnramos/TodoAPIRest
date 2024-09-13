package com.example.auth.middlewares

import com.example.auth.dtos.requests.LoginRequestDto
import com.example.auth.dtos.requests.RefreshRequestDto
import com.example.users.commands.GetByUsernameCommand
import com.example.users.services.implementations.UserServiceImpl

class AuthMiddleware(private val userService: UserServiceImpl) {
    private suspend fun isValidCredentials(request: LoginRequestDto) {
        val command = GetByUsernameCommand(request.username)
        val user = userService.getUserByUsername(command)

        if (request.username.isEmpty()) throw IllegalArgumentException("Username should not be empty")
        if (request.password.isEmpty()) throw IllegalArgumentException("Password should not be empty")

        if (user == null) throw IllegalArgumentException("Invalid credentials")
    }

    private fun refreshValidation(request: RefreshRequestDto) {
        if (request.refreshToken.isNullOrEmpty()) {
            throw IllegalArgumentException("Refresh token should not be empty")
        }
    }

    companion object {
        suspend fun validateAuthentication(request: LoginRequestDto, authMiddleware: AuthMiddleware) {
            authMiddleware.isValidCredentials(request)
        }

        fun validateRefresh(request: RefreshRequestDto, authMiddleware: AuthMiddleware) {
            authMiddleware.refreshValidation(request)
        }

    }
}