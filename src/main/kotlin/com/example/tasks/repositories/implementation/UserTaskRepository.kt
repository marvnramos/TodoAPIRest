package com.example.tasks.repositories.implementation

import com.example.plugins.DatabaseSingleton.Companion.dbQuery
import com.example.tasks.domain.models.UserTask
import com.example.tasks.entities.Tasks
import com.example.tasks.entities.UserTasks
import com.example.tasks.repositories.interfaces.IUserTaskRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.time.Instant
import java.util.*

class UserTaskRepository : IUserTaskRepository {
    override suspend fun insert(entity: UserTask): UserTask? = dbQuery {
        UserTasks.insert {
            it[userId] = entity.userId
            it[taskId] = entity.taskId
            it[assignedAt] = entity.assignedAt ?: Instant.now()
            it[archivedAt] = null
        }
        entity
    }

    override suspend fun getAllRelatedTasks(id: UUID): List<UserTask> = dbQuery {
        UserTasks.select { UserTasks.userId eq id }
            .map(::resultRowToUserTask)
    }

    override suspend fun getUserTasksByTaskId(id: UUID): List<UserTask> = dbQuery {
        UserTasks.select { UserTasks.taskId eq id }
            .map(::resultRowToUserTask)
    }

    override suspend fun replace(entity: UserTask): Boolean = dbQuery {
        val rowUpdated =
            UserTasks.update({ (UserTasks.userId eq entity.userId) and (UserTasks.taskId eq entity.taskId) }) {
                setTaskValues(it, entity)
            }
        rowUpdated > 0
    }

    override suspend fun delete(entity: UserTask): Boolean = dbQuery {
        UserTasks.deleteWhere { (userId eq entity.userId) and (taskId eq entity.taskId) } > 0
    }

    override suspend fun archive(entity: UserTask): Boolean = dbQuery {
        UserTasks.update({ (UserTasks.userId eq entity.userId) and (UserTasks.taskId eq entity.taskId) }) {
            it[archivedAt] = Instant.now()
        } > 0
    }

    override suspend fun getWhoImSharingWith(userId: UUID, taskId: UUID): List<UserTask> = dbQuery {
        val userTasks = (UserTasks innerJoin Tasks)
            .select {
                (UserTasks.taskId eq taskId) and (UserTasks.userId neq userId)
            }
            .map(::resultRowToUserTask)

        userTasks
    }

    override suspend fun getMySharedTasks(id: UUID): List<UserTask> = dbQuery {
        val userTasks = (UserTasks innerJoin Tasks)
            .select {
                UserTasks.userId eq id
            }
            .map(::resultRowToUserTask)

        userTasks.filter { task ->
            UserTasks
                .select {
                    (UserTasks.taskId eq task.taskId) and (UserTasks.userId neq id)
                }
                .count() > 0
        }
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