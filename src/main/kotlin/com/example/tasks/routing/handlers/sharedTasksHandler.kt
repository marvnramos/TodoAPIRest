package com.example.tasks.routing.handlers

import com.example.tasks.commands.GetSharedWithTasksCommand
import com.example.tasks.commands.HandleTaskCommand
import com.example.tasks.routing.exception.exceptionHandler
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

suspend fun sharedTasksHandler(sharedHandlerCommand: HandleTaskCommand) {
    val (call, _, userTaskService, _) = sharedHandlerCommand
    exceptionHandler(call) {
        // TODO: Get task data
        val principal = call.principal<JWTPrincipal>()
        val userId = UUID.fromString(principal?.payload?.getClaim("sub")?.asString())

        val command = GetSharedWithTasksCommand(userId)
        val tasks = userTaskService.getSharedTasks(command)

        call.respond(
            HttpStatusCode.OK, Json.encodeToString(tasks)
        )

    }
}