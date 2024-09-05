package com.example.users.services

import com.example.users.commands.*
import com.example.users.domain.models.User
import com.example.users.repositories.implementation.UserRepository
import com.example.users.services.interfaces.IUsersService

abstract class UserService(private val userRepository: UserRepository) : IUsersService {
    override suspend fun getUsers(command: GetUsersCommand): List<User> {
        return userRepository.getAll()
    }

    override suspend fun getUserById(command: GetUserByIdCommand): User? {
        return userRepository.findById(command.id)
    }

    override suspend fun getOneUser(command: GetOneUserCommand): User? {
        return userRepository.find(command.predicate)
    }

    override suspend fun createUser(command: CreateUserCommand): User? {
        val user = command.toEntity()
        userRepository.insert(user)
        return user
    }

    override suspend fun updateUser(command: UpdateUserCommand): User? {
        val user = userRepository.findById(command.id) ?: return null
        command.updateEntity(user)
        return user
    }

    override suspend fun updateUserPassword(command: UpdateUserPasswordCommand): Boolean {
        val user = userRepository.findById(command.id) ?: return false
        command.updatePassword(user)
        return true
    }

    override suspend fun getUserByEmail(command: GetByEmailCommand): User? {
        return userRepository.findByEmail(command.email)
    }

    override suspend fun getUserByUsername(command: GetByUsernameCommmand): User? {
        return userRepository.findByUsername(command.username)
    }
}