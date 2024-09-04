package com.example.users.commands

import java.util.UUID

data class UpdateUserPasswordCommand (
    val id: UUID,
    val password: String
)