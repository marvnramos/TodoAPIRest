package com.example.auth.middlewares

import com.example.users.middlewares.UserMiddleware

class AuthMiddleware {
//    suspend fun validateUsernameLogin(username: String?) {
//        if (username.isNullOrEmpty()) {
//            throw IllegalArgumentException("Username is required")
//        }
//        if (!existingUsername(username)) {
//            throw IllegalArgumentException("Invalid credentials")
//        }
//
//    }
//
//    fun validatePasswordLogin(password: String?) {
//        if (password.isNullOrEmpty()) {
//            throw IllegalArgumentException("Password is required")
//
//        }
//    }
//
//    companion object {
//
//        suspend fun validateUserLogin(request: LoginRequestDto, userMiddleware: UserMiddleware) {
//            userMiddleware.validateUsernameLogin(request.username)
//            userMiddleware.validatePasswordLogin(request.password)
//        }
//
//        fun validateRefresh(request: RefreshRequestDto) {
//            val (refreshToken) = request
//            if (refreshToken.isNullOrEmpty()) {
//                throw IllegalArgumentException("Missing refresh token")
//            }
//        }
//    }
}