package com.pahnal.mystoryapp.data.di

import android.content.Context
import com.pahnal.mystoryapp.data.repository.MyRepository
import com.pahnal.mystoryapp.data.source.local.room.StoryDatabase
import com.pahnal.mystoryapp.data.source.remote.network.ApiConfig
import com.pahnal.mystoryapp.domain.repository.IAuthRepository
import com.pahnal.mystoryapp.domain.repository.IStoryRepository

object Injection {


    fun provideIStoryRepository(context: Context): IStoryRepository {
        val apiService = ApiConfig.getInstance().getApiService()
        val database = StoryDatabase.getDatabase(context)

        return MyRepository.getInstance(database,apiService)
    }

    fun provideAuthRepository(context: Context): IAuthRepository {
        val apiService = ApiConfig.getInstance().getApiService()
        val database = StoryDatabase.getDatabase(context)

        return MyRepository.getInstance(database,apiService)
    }
}