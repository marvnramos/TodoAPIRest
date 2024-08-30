package com.example.plugins

import com.example.xd.entities.Tasks
import com.example.xd.entities.Users
import io.ktor.server.engine.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*

/**
 * TODO: Change entities
 */
class DatabaseSingleton(private val args: Array<String>) {
    private val env = commandLineEnvironment(args)
    private val appConfig = env.config

    fun init() {
        val driverClassName = "org.postgresql.Driver"

        val jdbcURL = appConfig.property("ktor.deployment.db.jdbcURL").getString()
        val user = appConfig.property("ktor.deployment.db.user").getString()
        val password = appConfig.property("ktor.deployment.db.password").getString()

        val database = Database.connect(
            url = jdbcURL,
            driver = driverClassName,
            user = user,
            password = password
        )

        transaction(database) {
            SchemaUtils.create(com.example.xd.entities.Tasks, com.example.xd.entities.Users)
        }
    }

    companion object{
        suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) {
            block()
        }
    }
}