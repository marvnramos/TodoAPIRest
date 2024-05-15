package com.example.services

import com.example.dao.DatabaseSingleton.dbQuery
import com.example.dao.interfaces.DAOUser
import com.example.models.users_models.User
import com.example.entities.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class UserService: DAOUser {

    private fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        username = row[Users.username],
        password = row[Users.password],
        createdAt = Date(row[Users.createdAt]),
        updatedAt = Date(row[Users.updatedAt])
    )

    override suspend fun addUser(user: User): User? = dbQuery{
        val insertStatement = Users.insert {
            it[id] = user.id!!
            it[username] = user.username
            it[password] = user.password
            it[createdAt] = user.createdAt!!.time
            it[updatedAt] = user.updatedAt!!.time
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
    }

    override suspend fun getUser(username: String): User? = dbQuery {
        Users
            .select(Users.username eq username)
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun getUser(id: UUID): User? = dbQuery{
        Users
            .select{ Users.id eq id }
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun editUser(id: UUID, user: User?): Boolean = dbQuery {
        val updatedRows = Users.update({ Users.id eq id }) {
            user?.username?.let { username ->
                it[Users.username] = username
            }
            user?.password?.let { password ->
                it[Users.password] = password
            }
            user?.updatedAt?.let { updatedAt ->
                it[Users.updatedAt] = updatedAt.time
            }
        }
        updatedRows > 0
    }

    override suspend fun deleteUser(id: UUID): Boolean = dbQuery {
        Users.deleteWhere { Users.id eq id } > 0
    }
}