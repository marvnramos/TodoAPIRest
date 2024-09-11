package com.example.auth.commands

data class CreateRefreshTokenCommand(
    val subject: String,
    val issuer: String,
    val claim: String,
    val expirationDate: Long,
    val signature: String
)
