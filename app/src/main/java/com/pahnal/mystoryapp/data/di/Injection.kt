package com.pahnal.mystoryapp.data.di

import com.pahnal.mystoryapp.data.repository.MyRepository
import com.pahnal.mystoryapp.data.source.remote.network.ApiConfig
import com.pahnal.mystoryapp.domain.repository.IAuthRepository
import com.pahnal.mystoryapp.domain.repository.IStoryRepository

object Injection {


    fun provideIStoryRepository(): IStoryRepository {
        val apiService = ApiConfig.getInstance().getApiService()

        return MyRepository.getInstance(apiService)
    }

    fun provideAuthRepository(): IAuthRepository {
        val apiService = ApiConfig.getInstance().getApiService()

        return MyRepository.getInstance(apiService)
    }
}