package com.example.dao.interfaces

import com.example.models.users_models.User
import java.util.UUID

interface DAOUser {
    suspend fun addUser(user: User): User?
    suspend fun getUser(id: UUID): User?
    suspend fun editUser(id: UUID, user: User?): Boolean
    suspend fun  deleteUser(id: UUID): Boolean
}