package com.example.xd.services

import com.example.xd.dao.DatabaseSingleton.Companion.dbQuery
import com.example.xd.dao.interfaces.DAOTask
import com.example.xd.entities.Tasks
import com.example.models.task_models.Task
import com.example.models.task_models.TaskPartial
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class TaskService: com.example.xd.dao.interfaces.DAOTask {
    private fun resultRowToTask(row: ResultRow) = Task(
        id = row[com.example.xd.entities.Tasks.id],
        title = row[com.example.xd.entities.Tasks.title],
        description = row[com.example.xd.entities.Tasks.description],
        status = row[com.example.xd.entities.Tasks.status],
        icon = row[com.example.xd.entities.Tasks.icon],
        dueDate = Date(row[com.example.xd.entities.Tasks.dueDate]),
        userId = row[com.example.xd.entities.Tasks.userId],
        createdAt = Date(row[com.example.xd.entities.Tasks.createdAt]),
        updatedAt = Date(row[com.example.xd.entities.Tasks.updatedAt])
    )

    override suspend fun allTasks(): List<Task> = dbQuery{
        com.example.xd.entities.Tasks
            .selectAll()
            .map ( ::resultRowToTask )
    }

    override suspend fun getTask(id: UUID): Task? = dbQuery{
        com.example.xd.entities.Tasks
            .select { com.example.xd.entities.Tasks.id eq id }
            .map ( ::resultRowToTask )
            .singleOrNull()
    }

    override suspend fun addTask(task: Task): Task? = dbQuery {
        val insertStatement = com.example.xd.entities.Tasks.insert {
            it[id] = task.id!!
            it[title] = task.title
            it[description] = task.description
            it[status] = task.status
            it[icon] = task.icon
            it[dueDate] = task.dueDate.time
            it[userId] = task.userId!!
            it[createdAt] = task.createdAt!!.time
            it[updatedAt] = task.updatedAt!!.time
        }
        insertStatement.resultedValues?.singleOrNull()?.let( ::resultRowToTask )
    }

    override suspend fun editTask(id: UUID, task: TaskPartial): Boolean = dbQuery {
        val updatedRows = com.example.xd.entities.Tasks.update({ com.example.xd.entities.Tasks.id eq id }) {
            task.title?.let{ title ->
                it[com.example.xd.entities.Tasks.title] = title
            }
            task.description?.let{ description ->
                it[com.example.xd.entities.Tasks.description] = description
            }
            task.status?.let { status ->
                it[com.example.xd.entities.Tasks.status] = status
            }
            task.icon?.let { icon ->
                it[com.example.xd.entities.Tasks.icon] = icon
            }
            task.dueDate?.let { dueDate ->
                it[com.example.xd.entities.Tasks.dueDate] = dueDate.time
            }
            task.updatedAt?.let { updatedAt ->
                it[com.example.xd.entities.Tasks.updatedAt] = updatedAt.time
            }
        }
        updatedRows > 0
    }

    override suspend fun deleteTask(id: UUID): Boolean = dbQuery{
        com.example.xd.entities.Tasks.deleteWhere { com.example.xd.entities.Tasks.id eq id } > 0
    }

}