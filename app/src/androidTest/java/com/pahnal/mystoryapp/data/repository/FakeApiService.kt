package com.pahnal.mystoryapp.data.repository

import com.pahnal.mystoryapp.data.source.remote.dto.request.LoginRequestBodyDto
import com.pahnal.mystoryapp.data.source.remote.dto.request.RegisterRequestBodyDto
import com.pahnal.mystoryapp.data.source.remote.dto.response.BasicResponseDto
import com.pahnal.mystoryapp.data.source.remote.dto.response.GetAllStoriesResponseDto
import com.pahnal.mystoryapp.data.source.remote.dto.response.LoginResponseDto
import com.pahnal.mystoryapp.data.source.remote.network.ApiService
import com.pahnal.mystoryapp.utils.DataDummy
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService : ApiService {

    override suspend fun register(registerRequestBodyDto: RegisterRequestBodyDto): BasicResponseDto {
        return DataDummy.generateRegisterSuccess()
    }

    override suspend fun login(loginRequestBodyDto: LoginRequestBodyDto): LoginResponseDto {
        return DataDummy.generateDummyLoginSuccess()
    }

    override suspend fun getAllStories(
        token: String,
        location: Int,
        page: Int,
        size: Int
    ): GetAllStoriesResponseDto {
        return DataDummy.generateDummyStoriesResponse(page, size)
    }

    override suspend fun addNewStory(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?,
        token: String
    ): BasicResponseDto {
        return DataDummy.generateAddStorySuccess()
    }
}