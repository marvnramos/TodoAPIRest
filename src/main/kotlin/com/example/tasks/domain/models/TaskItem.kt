package com.example.tasks.domain.models

import kotlinx.serialization.Serializable


@Serializable
sealed class TaskItem {
    @Serializable
    data class PersonalTask(val task: Task) : TaskItem()

    @Serializable
    data class SharedTask(val task: com.example.tasks.domain.models.SharedTask) : TaskItem()
}