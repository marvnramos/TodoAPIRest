package com.example.tasks.repositories.implementation

import com.example.plugins.DatabaseSingleton.Companion.dbQuery
import com.example.tasks.domain.models.Task
import com.example.tasks.entities.Tasks
import com.example.tasks.repositories.interfaces.ITaskRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.time.Instant
import java.util.*

class TaskRepository : ITaskRepository {
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
            setTaskValues(it, entity)
        }
        rowsUpdated > 0
    }

    override suspend fun find(predicate: (Task) -> Boolean): Task? = dbQuery {
        Tasks
            .selectAll()
            .map(::resultRowToTask).find(predicate)
    }

    override suspend fun insert(entity: Task): Task? = dbQuery {
        Tasks.insert {
            it[id] = entity.id!!
            setTaskValues(it, entity)
            it[createdAt] = entity.createdAt ?: Instant.now()
        }
        entity
    }

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

    private fun setTaskValues(statement: UpdateBuilder<*>, entity: Task) {
        statement[Tasks.title] = entity.title
        statement[Tasks.description] = entity.description ?: return
        statement[Tasks.status] = entity.status
        statement[Tasks.dueDate] = entity.dueDate
        statement[Tasks.createdBy] = entity.createdBy!!
        statement[Tasks.updatedAt] = entity.updatedAt ?: Instant.now()
    }
}