package com.example.dao.implementations

import com.example.dao.DatabaseSingleton.dbQuery
import com.example.dao.interfaces.DAOUser
import com.example.models.users_models.User
import com.example.models.users_models.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class DAOUserImpl: DAOUser {
    private fun Long.toDate(): Date {
        return Date(this)
    }

    private fun Date.toLong(): Long {
        return  this.time
    }

    private fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        username = row[Users.username],
        password = row[Users.password],
        createdAt = row[Users.createdAt].toDate(),
        updatedAt = row[Users.updatedAt].toDate()
    )

    override suspend fun addUser(user: User): User? = dbQuery{
        val insertStatement = Users.insert {
            it[Users.id] = user.id
            it[Users.username] = user.username
            it[Users.password] = user.password
            it[Users.createdAt] = user.createdAt.toLong()
            it[Users.updatedAt] = user.updatedAt.toLong()
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
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
                it[Users.updatedAt] = updatedAt.toLong()
            }
        }
        updatedRows > 0
    }

    override suspend fun deleteUser(id: UUID): Boolean = dbQuery {
        Users.deleteWhere { Users.id eq id } > 0
    }
}