package com.pahnal.mystoryapp.data.source.remote.network

import com.pahnal.mystoryapp.data.source.remote.dto.request.LoginRequestBodyDto
import com.pahnal.mystoryapp.data.source.remote.dto.request.RegisterRequestBodyDto
import com.pahnal.mystoryapp.data.source.remote.dto.response.BasicResponseDto
import com.pahnal.mystoryapp.data.source.remote.dto.response.GetAllStoriesResponseDto
import com.pahnal.mystoryapp.data.source.remote.dto.response.LoginResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @POST("register")
    suspend fun register(@Body registerRequestBodyDto: RegisterRequestBodyDto): BasicResponseDto

    @POST("login")
    suspend fun login(@Body loginRequestBodyDto: LoginRequestBodyDto): LoginResponseDto

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("location") location: Int,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): GetAllStoriesResponseDto

    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?,
        @Header("authorization") token: String,
    ): BasicResponseDto
}