package com.example.auth.services.implementations

import com.example.auth.repositories.implementation.AuthRepositoryImpl
import com.example.auth.services.AuthService

class AuthServiceImpl(authRepository: AuthRepositoryImpl) : AuthService(authRepository)