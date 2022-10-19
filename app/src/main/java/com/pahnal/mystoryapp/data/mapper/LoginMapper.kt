package com.pahnal.mystoryapp.data.mapper

import com.pahnal.mystoryapp.data.source.remote.dto.request.LoginRequestBodyDto
import com.pahnal.mystoryapp.data.source.remote.dto.response.LoginResponseDto
import com.pahnal.mystoryapp.domain.model.LoginRequest
import com.pahnal.mystoryapp.domain.model.User

fun LoginRequest.toDto(): LoginRequestBodyDto = LoginRequestBodyDto(
    email, password
)

fun LoginResponseDto.toDomain() = User(
    name = loginResult?.name ?: "",
    userId = loginResult?.userId ?: "",
    token = loginResult?.token ?: "",
)