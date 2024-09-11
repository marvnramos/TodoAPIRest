package com.example.users.services.interfaces

import com.example.users.commands.*
import com.example.users.domain.models.User

interface IUsersService {
    suspend fun getUsers(command: GetUsersCommand): List<User>
    suspend fun getUserById(command: GetUserByIdCommand): User?
    suspend fun getOneUser(command: GetOneUserCommand): User?
    suspend fun createUser(command: CreateUserCommand): User?
    suspend fun updateUser(command: UpdateUserCommand): User?
    suspend fun updateUserPassword(command: UpdateUserPasswordCommand): Boolean
    suspend fun getUserByEmail(command: GetByEmailCommand): User?
    suspend fun getUserByUsername(command: GetByUsernameCommand): User?
}