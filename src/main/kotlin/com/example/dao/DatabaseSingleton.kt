package com.example.dao

import com.example.entities.Tasks
//import com.example.models.users_models.Users
import com.example.entities.Users
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*


object DatabaseSingleton {
    fun init() {
        val driverClassName = "org.postgresql.Driver"
        val jdbcURL = "jdbc:postgresql://ep-rough-limit-a4sh9gft.us-east-1.aws.neon.tech:5432/verceldb?sslmode=require"
        val user = "default"
        val password = "GUByd2Fx9fsH"

        val database = Database.connect(
            url = jdbcURL,
            driver = driverClassName,
            user = user,
            password = password
        )

        transaction(database) {
            SchemaUtils.create(Tasks, Users)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) {
        block()
    }
}