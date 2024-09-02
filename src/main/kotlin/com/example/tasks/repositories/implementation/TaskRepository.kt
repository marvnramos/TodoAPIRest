package com.example.tasks.repositories.implementation

import com.example.plugins.DatabaseSingleton.Companion.dbQuery
import com.example.tasks.domain.models.Task
import com.example.tasks.entities.Tasks
import com.example.tasks.repositories.interfaces.ITaskRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class TaskRepository : ITaskRepository {

    private fun resultRowToTask(row: ResultRow): Task = Task(
        id = row[Tasks.id],
        title = row[Tasks.title],
        description = row[Tasks.description],
        status = row[Tasks.status],
        dueDate = row[Tasks.dueDate],
        createdBy = row[Tasks.createdBy],
        createdAt = row[Tasks.createdAt],
        updatedAt = row[Tasks.updatedAt]
    )

    override suspend fun getAll(): List<Task> = dbQuery {
        Tasks
            .selectAll()
            .map(::resultRowToTask)
    }

    override suspend fun findById(id: UUID): Task? = dbQuery {
        Tasks
            .select { Tasks.id eq id }
            .map { resultRowToTask(it) }
            .singleOrNull()
    }

    override suspend fun delete(id: UUID): Boolean = dbQuery {
        Tasks.deleteWhere { Tasks.id eq id } > 0
    }

    override suspend fun replace(entity: Task): Boolean = dbQuery {
        val rowsUpdated = Tasks.update({ Tasks.id eq entity.id!! }) {
            it[title] = entity.title
            it[description] = entity.description
            it[status] = entity.status
            it[dueDate] = entity.dueDate
            it[createdBy] = entity.createdBy!!
            it[updatedAt] = entity.updatedAt
        }
        rowsUpdated > 0
    }

    override suspend fun find(predicate: (Task) -> Boolean): Task? = dbQuery {
        Tasks
            .selectAll()
            .map (::resultRowToTask).find (predicate)
    }

    override suspend fun insert(entity: Task): Task? = dbQuery {
        Tasks.insert {
            it[id] = entity.id!!
            it[title] = entity.title
            it[description] = entity.description
            it[status] = entity.status
            it[dueDate] = entity.dueDate
            it[createdBy] = entity.createdBy!!
            it[createdAt] = entity.createdAt
            it[updatedAt] = entity.updatedAt
        }
        entity
    }
}
