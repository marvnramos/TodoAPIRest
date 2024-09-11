package com.example.auth.commands

data class CreateAccessTokenCommand (
    val subject: String,
    val audience: String,
    val issuer: String,
    val claim: String,
    val expirationDate: Long,
    val signature: String
)