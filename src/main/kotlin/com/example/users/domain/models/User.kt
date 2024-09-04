package com.example.users.domain.models

import com.example.commons.models.Entity
import com.example.commons.serializers.InstantSerializer
import com.example.commons.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.*

@Serializable
data class User(
    @Serializable(with = UUIDSerializer::class)
    override val id: UUID = UUID.randomUUID(),
    val username: String,
    val email: String,
    val profilePhoto: String?,
    val password: String,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant?,
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant?
): Entity(id)