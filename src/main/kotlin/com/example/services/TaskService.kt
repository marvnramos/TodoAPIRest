package com.example.services

import com.example.dao.DatabaseSingleton.dbQuery
import com.example.dao.interfaces.DAOTask
import com.example.entities.Tasks
import com.example.entities.Users
import com.example.models.task_models.Task
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class TaskService:DAOTask {
    private fun resultRowToTask(row: ResultRow) = Task(
        id = row[Tasks.id],
        title = row[Tasks.title],
        description = row[Tasks.description],
        status = row[Tasks.status],
        icon = row[Tasks.icon],
        dueDate = Date(row[Tasks.dueDate]),
        userId = row[Tasks.userId],
        createdAt = Date(row[Tasks.createdAt]),
        updatedAt = Date(row[Tasks.updatedAt])
    )

    override suspend fun allTasks(): List<Task> = dbQuery{
        Tasks
            .selectAll()
            .map ( ::resultRowToTask )
    }

    override suspend fun getTask(id: UUID): Task? = dbQuery{
        Tasks
            .select { Tasks.id eq id }
            .map ( ::resultRowToTask )
            .singleOrNull()
    }

    override suspend fun addTask(task: Task): Task? = dbQuery {
        val insertStatement = Tasks.insert {
            it[id] = task.id!!
            it[title] = task.title
            it[description] = task.description
            it[status] = task.status
            it[icon] = task.icon
            it[dueDate] = task.dueDate.time
            it[userId] = task.userId
            it[createdAt] = task.createdAt!!.time
            it[updatedAt] = task.updatedAt!!.time
        }
        insertStatement.resultedValues?.singleOrNull()?.let( ::resultRowToTask )
    }

    override suspend fun editTask(id: UUID, task: Task?): Boolean = dbQuery {
        val updatedRows = Tasks.update({ Tasks.id eq id }) {
            task?.title?.let{ title ->
                it[Tasks.title] = title
            }
            task?.description?.let{ description ->
                it[Tasks.description] = description
            }
            task?.status?.let { status ->
                it[Tasks.status] = status
            }
            task?.icon?.let { icon ->
                it[Tasks.icon] = icon
            }
            task?.dueDate?.let { dueDate ->
                it[Tasks.dueDate] = dueDate.time
            }
            task?.userId?.let { userId ->
                it[Tasks.userId] = userId
            }
            task?.updatedAt?.let { updatedAt ->
                it[Tasks.updatedAt] = updatedAt.time
            }
        }
        updatedRows > 0;
    }

    override suspend fun deleteTask(id: UUID): Boolean = dbQuery{
        Tasks.deleteWhere { Tasks.id eq id } > 0
    }

}