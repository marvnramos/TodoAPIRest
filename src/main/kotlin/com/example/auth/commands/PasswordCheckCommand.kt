package com.example.auth.commands

data class PasswordCheckCommand (
    val password: String,
    val realPassword: String
)