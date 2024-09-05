package com.example.users.commands

import com.example.users.domain.models.User
import java.time.Instant
import java.util.UUID

data class UpdateUserPasswordCommand(
    val id: UUID,
    val password: String,
) {
    fun updatePassword(user: User): User {
        return user.copy(
            password = password,
            updatedAt = Instant.now()
        )
    }
}