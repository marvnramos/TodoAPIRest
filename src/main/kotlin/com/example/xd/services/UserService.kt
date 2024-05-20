package com.example.xd.services

import com.example.xd.dao.DatabaseSingleton.Companion.dbQuery
import com.example.xd.dao.interfaces.DAOUser
import com.example.models.users_models.User
import com.example.xd.entities.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class UserService: com.example.xd.dao.interfaces.DAOUser {

    private fun resultRowToUser(row: ResultRow) = User(
        id = row[com.example.xd.entities.Users.id],
        username = row[com.example.xd.entities.Users.username],
        password = row[com.example.xd.entities.Users.password],
        createdAt = Date(row[com.example.xd.entities.Users.createdAt]),
        updatedAt = Date(row[com.example.xd.entities.Users.updatedAt])
    )

    override suspend fun addUser(user: User): User? = dbQuery{
        val insertStatement = com.example.xd.entities.Users.insert {
            it[id] = user.id!!
            it[username] = user.username
            it[password] = user.password
            it[createdAt] = user.createdAt!!.time
            it[updatedAt] = user.updatedAt!!.time
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
    }

    override suspend fun getUser(username: String): User? = dbQuery {
        com.example.xd.entities.Users
            .select(com.example.xd.entities.Users.username eq username)
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun getUser(id: UUID): User? = dbQuery{
        com.example.xd.entities.Users
            .select{ com.example.xd.entities.Users.id eq id }
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun editUser(id: UUID, user: User?): Boolean = dbQuery {
        val updatedRows = com.example.xd.entities.Users.update({ com.example.xd.entities.Users.id eq id }) {
            user?.username?.let { username ->
                it[com.example.xd.entities.Users.username] = username
            }
            user?.password?.let { password ->
                it[com.example.xd.entities.Users.password] = password
            }
            user?.updatedAt?.let { updatedAt ->
                it[com.example.xd.entities.Users.updatedAt] = updatedAt.time
            }
        }
        updatedRows > 0
    }

    override suspend fun deleteUser(id: UUID): Boolean = dbQuery {
        com.example.xd.entities.Users.deleteWhere { com.example.xd.entities.Users.id eq id } > 0
    }
}