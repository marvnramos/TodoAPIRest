package com.example.models

import kotlinx.serialization.Serializable

@Serializable
class AuthModel(
    val message: String = "Welcome, You're logged in!",
    var token: String
)