package com.example.users.middlewares

import com.example.users.commands.GetByEmailCommand
import com.example.users.commands.GetByUsernameCommmand
import com.example.users.dtos.requests.AddRequestDto
import com.example.users.services.implementations.UserServiceImpl

class UserMiddleware(private val userService: UserServiceImpl) {

    private val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private val passwordRegex = Regex("^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}\$")

    private fun isValidEmail(email: String): Boolean = emailRegex.matches(email)
    private fun isValidPassword(password: String): Boolean = passwordRegex.matches(password)

    private suspend fun existingEmail(email: String): Boolean {
        val command = GetByEmailCommand(email)
        val user = userService.getUserByEmail(command)
        return user != null
    }

    private suspend fun existingUsername(username: String): Boolean {
        val command = GetByUsernameCommmand(username)
        val user = userService.getUserByUsername(command)
        return user != null
    }

    suspend fun validateEmail(email: String?) {
        if (email.isNullOrEmpty()) {
            throw IllegalArgumentException("Email is required")
        }
        if (!isValidEmail(email)) {
            throw IllegalArgumentException("Invalid email")
        }
        if (existingEmail(email)) {
            throw IllegalArgumentException("Email already in use")
        }
    }

    suspend fun validateUsername(username: String?) {
        if (username.isNullOrEmpty()) {
            throw IllegalArgumentException("Username is required")
        }
        if (existingUsername(username)) {
            throw IllegalArgumentException("Username already in use")
        }
    }

    suspend fun validatePassword(password: String?) {
        if (password.isNullOrEmpty()) {
            throw IllegalArgumentException("Password is required")
        }

        if (password.isBlank()) {
            throw IllegalArgumentException("Password cannot be blank")
        }

        if (!isValidPassword(password)) {
            throw IllegalArgumentException("Password is not valid")
        }

        if (existingUsername(password)) {
            throw IllegalArgumentException("Username cannot be password")
        }

        println(password)
    }

    companion object {
        suspend fun validateUser(request: AddRequestDto, userMiddleware: UserMiddleware) {
            userMiddleware.validateUsername(request.username)
            userMiddleware.validateEmail(request.email)
            userMiddleware.validatePassword(request.password)
        }
    }
}