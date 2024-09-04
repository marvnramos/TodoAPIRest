package com.example.users.commands

import java.util.UUID

data class UpdateUserCommand(
    val id: UUID,
    val username: String? = null,
    val email: String? = null,
    val profilePhoto: String? = null,
)