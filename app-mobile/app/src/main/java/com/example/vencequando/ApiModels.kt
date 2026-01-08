package com.example.vencequando

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val message: String,
    val user: UserData?
)

data class UserData(
    val id: Int,
    val name: String,
    val email: String
)