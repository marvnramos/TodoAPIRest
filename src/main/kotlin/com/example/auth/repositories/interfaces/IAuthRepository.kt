package com.example.auth.repositories.interfaces

import com.example.auth.domain.models.JWT

interface IAuthRepository {
    suspend fun refreshToken(entity: JWT, username: String): String
    suspend fun accessToken(entity: JWT, username: String): String
    suspend fun validateToken(token: String): Any
    suspend fun checkPassword(password: String, username: String)
}