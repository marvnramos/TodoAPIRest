package com.example.users.commands

import com.example.users.middlewares.UserMiddleware
import com.example.users.services.implementations.UserServiceImpl
import io.ktor.server.application.*

data class HandleUserCommand(
    val call: ApplicationCall,
    val userService: UserServiceImpl,
    val userMiddleware: UserMiddleware
)