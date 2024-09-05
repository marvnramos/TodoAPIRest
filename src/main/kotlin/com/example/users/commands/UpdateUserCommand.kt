package com.example.users.commands

import com.example.users.domain.models.User
import java.time.Instant
import java.util.UUID

data class UpdateUserCommand(
    val id: UUID,
    val username: String? = null,
    val email: String? = null,
    val profilePhoto: String? = null,
) {
    fun updateEntity(user: User): User {
        return user.copy(
            username = this.username ?: user.username,
            email = this.email ?: user.email,
            profilePhoto = this.profilePhoto ?: user.profilePhoto,
            updatedAt = Instant.now()
        )
    }
}