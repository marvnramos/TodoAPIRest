package com.example.users.repositories.implementation

import com.example.plugins.DatabaseSingleton.Companion.dbQuery
import com.example.users.domain.models.User
import com.example.users.entities.Users
import com.example.users.repositories.interfaces.IUserRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import java.util.*

class UserRepository : IUserRepository {
    override suspend fun getAll(): List<User> = dbQuery {
        Users
            .selectAll()
            .map(::resultRowToUser)
    }

    override suspend fun findById(id: UUID): User? {
        TODO("Not yet implemented")
    }

    override suspend fun insert(entity: User): User? {
        TODO("Not yet implemented")
    }

    override suspend fun find(predicate: (User) -> Boolean): User? {
        TODO("Not yet implemented")
    }

    override suspend fun replace(entity: User): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: UUID): Boolean {
        TODO("Not yet implemented")
    }

    private fun resultRowToUser(row: ResultRow): User = User(
        id = row[Users.id],
        username = row[Users.username],
        email = row[Users.email],
        profilePhoto = row[Users.profilePhoto],
        password = row[Users.password],
        createdAt = row[Users.createdAt],
        updatedAt = row[Users.updatedAt]
    )
}