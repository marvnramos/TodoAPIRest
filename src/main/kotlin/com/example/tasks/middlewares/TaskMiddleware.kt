package com.example.tasks.middlewares

import com.example.tasks.dtos.requests.AddRequestDto

class TaskMiddleware {
    private fun validatePayload(request: AddRequestDto) {
        val errors = mutableListOf<String>()

        if (request.title.isEmpty()) errors.add("Title can't be empty")
        if (request.description.isNullOrEmpty()) errors.add("Description can't be empty")
        if (request.dueDate.toString().isEmpty()) errors.add("Due date can't be empty")
        if (request.statusId.toString().isEmpty()) errors.add("Status id can't be empty")
        if (request.priorityId.toString().isEmpty()) errors.add("Priority id can't be empty")

        if (errors.isNotEmpty()) {
            throw IllegalArgumentException(errors.joinToString(", "))
        }
    }

    companion object {
        fun taskValidator(request: AddRequestDto, taskMiddleware: TaskMiddleware) {
            taskMiddleware.validatePayload(request)
        }
    }
}