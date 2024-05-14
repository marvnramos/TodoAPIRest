package com.example.services

import com.example.dao.DatabaseSingleton.dbQuery
import com.example.dao.interfaces.DAOTask
import com.example.entities.Tasks
import com.example.models.task_models.Task
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
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
            it[Tasks.id] = task.id!!
            it[Tasks.title] = task.title
            it[Tasks.description] = task.description
            it[Tasks.status] = task.status
            it[Tasks.icon] = task.icon
            it[Tasks.dueDate] = task.dueDate.time
            it[Tasks.userId] = task.userId
            it[Tasks.createdAt] = task.createdAt!!.time
            it[Tasks.updatedAt] = task.updatedAt!!.time
        }
        insertStatement.resultedValues?.singleOrNull()?.let( ::resultRowToTask )
    }

    override suspend fun editTask(id: UUID, task: Task?): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(id: UUID): Boolean {
        TODO("Not yet implemented")
    }

}