package com.example.users.commands

import com.example.users.domain.models.User

data class GetOneUserCommand(val predicate: (User) -> Boolean)
