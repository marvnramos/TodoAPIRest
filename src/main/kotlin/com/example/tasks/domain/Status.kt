package com.example.tasks.domain

import kotlinx.serialization.Serializable

@Serializable
enum class Status {
    IN_PROGRESS,
    TODO,
    DONE
}