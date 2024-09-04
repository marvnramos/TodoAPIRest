package com.example.users.commands

import com.example.users.domain.models.User
import java.time.Instant

data class CreateUserCommand(
    val username: String,
    val email: String,
    val profilePhoto: String?,
    val password: String
){
    fun toEntity(): User{
        return User(
            username = username,
            email = email,
            profilePhoto = profilePhoto,
            password = password,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )
    }
}