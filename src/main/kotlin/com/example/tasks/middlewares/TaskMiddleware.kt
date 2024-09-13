package com.example.tasks.middlewares

import com.example.tasks.dtos.requests.AddRequestDto

class TaskMiddleware {
    private fun validatePayload(request: AddRequestDto) {
        val errors = mutableListOf<String>()

        if (request.title.isEmpty()) errors.add("Title can't be empty")
        if (request.description.isNullOrEmpty()) errors.add("Description can't be empty")
        if (request.dueDate.toString().isEmpty()) errors.add("Due date can't be empty")
        if (request.status.toString().isEmpty()) errors.add("Status can't be empty")
        if (request.priority.toString().isEmpty()) errors.add("Priority can't be empty")

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