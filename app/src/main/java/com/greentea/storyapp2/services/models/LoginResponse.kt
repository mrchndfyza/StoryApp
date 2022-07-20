package com.greentea.storyapp2.services.models

data class LoginResponse(
    val error: Boolean,
    val loginResult: LoginResult,
    val message: String
)