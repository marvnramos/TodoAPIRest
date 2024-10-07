package com.example.tasks.commands

import java.util.*

data class GetWhoImSharingWIthCommand(
    val userId: UUID,
    val taskId: UUID
)
