package com.pahnal.mystoryapp.data.mapper

import com.pahnal.mystoryapp.data.source.remote.dto.request.RegisterRequestBodyDto
import com.pahnal.mystoryapp.domain.model.RegisterRequest

fun RegisterRequest.toDto(): RegisterRequestBodyDto = RegisterRequestBodyDto(
    name,
    email,
    password
)