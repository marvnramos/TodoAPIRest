package com.example.tasks.commands

import java.util.UUID

data class GetByTaskIdCommand (
    val taskId: UUID
)