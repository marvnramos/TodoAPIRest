package com.example.users.commands

import com.example.commons.serializers.UUIDSerializer
import com.example.users.domain.models.User
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.*

data class CreateUserCommand(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val username: String,
    val email: String,
    val profilePhoto: String?,
    val password: String
) {
    fun toEntity(): User {
        return User(
            id = id,
            username = username,
            email = email,
            profilePhoto = profilePhoto,
            password = password,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )
    }
}