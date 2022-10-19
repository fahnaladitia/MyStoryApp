package com.pahnal.mystoryapp.domain.model

data class LoginRequest(
    val email: String,
    val password: String,
)