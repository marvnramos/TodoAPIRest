package com.example.users.commands

import java.util.UUID

data class ForgotPasswordUserCommand(
    val id: UUID,
    val email: String
)
