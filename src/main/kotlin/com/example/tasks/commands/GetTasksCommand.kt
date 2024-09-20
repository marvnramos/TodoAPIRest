package com.example.tasks.commands

import java.util.UUID

data class GetTasksCommand(
    val userId: UUID
)