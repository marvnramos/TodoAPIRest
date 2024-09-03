package com.example.users.repositories.implementation

import com.example.plugins.DatabaseSingleton.Companion.dbQuery
import com.example.users.domain.models.User
import com.example.users.entities.Users
import com.example.users.repositories.interfaces.IUserRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.time.Instant
import java.util.*

class UserRepository : IUserRepository {
    override suspend fun getAll(): List<User> = dbQuery {
        Users
            .selectAll()
            .map(::resultRowToUser)
    }

    override suspend fun findById(id: UUID): User? = dbQuery {
        Users
            .select { Users.id eq id }
            .map { resultRowToUser(it) }
            .singleOrNull()
    }

    override suspend fun insert(entity: User): User? = dbQuery {
        Users.insert {
            it[id] = entity.id
            setUserValues(it, entity)
            it[createdAt] = entity.createdAt ?: Instant.now()
        }
        entity
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

    private fun setUserValues(statement: UpdateBuilder<*>, entity: User) {
        statement[Users.username] = entity.username
        statement[Users.email] = entity.email
        statement[Users.profilePhoto] = entity.profilePhoto
        statement[Users.password] = entity.password
        statement[Users.updatedAt] = entity.updatedAt ?: Instant.now()

    }
}