package com.example.tasks.repositories.implementation

import com.example.plugins.DatabaseSingleton.Companion.dbQuery
import com.example.tasks.domain.models.UserTask
import com.example.tasks.entities.UserTasks
import com.example.tasks.repositories.interfaces.IUserTaskRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.time.Instant
import java.util.*

class UserTaskRepository : IUserTaskRepository {
    override suspend fun getAll(): List<UserTask> = dbQuery {
        UserTasks
            .selectAll()
            .map(::resultRowToUserTask)
    }

    override suspend fun findById(id: UUID): UserTask? = dbQuery {
        UserTasks
            .select { (UserTasks.userId eq id) or (UserTasks.taskId eq id) }
            .map { resultRowToUserTask(it) }
            .singleOrNull()
    }

    override suspend fun insert(entity: UserTask): UserTask? = dbQuery {
        UserTasks.insert {
            it[userId] = entity.userId
            it[taskId] = entity.taskId
            it[assignedAt] = entity.assignedAt ?: Instant.now()
            it[archivedAt] = entity.archivedAt
        }
        entity
    }

    override suspend fun find(predicate: (UserTask) -> Boolean): UserTask? = dbQuery {
        UserTasks
            .selectAll()
            .map(::resultRowToUserTask)
            .find(predicate)
    }

    override suspend fun replace(entity: UserTask): Boolean = dbQuery {
        val rowUpdated =
            UserTasks.update({ (UserTasks.userId eq entity.userId) and (UserTasks.taskId eq entity.taskId) }) {
                setTaskValues(it, entity)
            }
        rowUpdated > 0
    }

    override suspend fun delete(id: UUID): Boolean = dbQuery {
        UserTasks.deleteWhere { (userId eq id) and (taskId eq id) } > 0
    }

    private fun resultRowToUserTask(row: ResultRow): UserTask = UserTask(
        userId = row[UserTasks.userId],
        taskId = row[UserTasks.taskId],
        assignedAt = row[UserTasks.assignedAt],
        archivedAt = row[UserTasks.archivedAt]
    )

    private fun setTaskValues(it: UpdateBuilder<*>, entity: UserTask) {
        it[UserTasks.userId] = entity.userId
        it[UserTasks.taskId] = entity.taskId
        it[UserTasks.assignedAt] = entity.assignedAt ?: Instant.now()
        it[UserTasks.archivedAt] = entity.archivedAt
    }
}