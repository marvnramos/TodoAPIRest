package com.example.commons.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
open class Entity(
    @Transient
    open val id: UUID? = UUID.randomUUID()
)