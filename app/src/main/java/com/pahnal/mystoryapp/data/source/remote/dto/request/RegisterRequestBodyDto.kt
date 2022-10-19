package com.pahnal.mystoryapp.data.source.remote.dto.request

data class RegisterRequestBodyDto(
    val name: String,
    val email: String,
    val password: String,
)