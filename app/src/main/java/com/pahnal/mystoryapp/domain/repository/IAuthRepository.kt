package com.pahnal.mystoryapp.domain.repository

import com.pahnal.mystoryapp.data.vo.Resource
import com.pahnal.mystoryapp.domain.model.LoginRequest
import com.pahnal.mystoryapp.domain.model.RegisterRequest
import com.pahnal.mystoryapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {
    suspend fun login(login: LoginRequest): Flow<Resource<User>>
    suspend fun register(register: RegisterRequest): Flow<Resource<String>>
}