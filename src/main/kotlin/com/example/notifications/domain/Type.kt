package com.example.notifications.domain

import kotlinx.serialization.Serializable

@Serializable
enum class Type {
    FRIEND_REQUEST,
    TASK_ASSIGNED,
    TASK_COMPLETED,
    TASK_DUE,
    FRIEND_ACCEPTED,
    TASK_ARCHIVED
}