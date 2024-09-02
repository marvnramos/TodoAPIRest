package com.example.tasks.commands

import com.example.tasks.domain.models.Task

data class GetOneTaskCommand(val predicate: (Task) -> Boolean)