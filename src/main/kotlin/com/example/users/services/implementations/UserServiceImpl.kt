package com.example.users.services.implementations

import com.example.users.repositories.implementation.UserRepository
import com.example.users.services.UserService

class UserServiceImpl(userRepository: UserRepository) : UserService(userRepository)