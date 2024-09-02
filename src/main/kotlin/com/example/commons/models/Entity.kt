package com.example.commons.models

import com.example.commons.serializers.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
open class Entity(
    @Serializable(with = UUIDSerializer::class)
    @SerialName("entity_id")
    open val id: UUID? = UUID.randomUUID()
)