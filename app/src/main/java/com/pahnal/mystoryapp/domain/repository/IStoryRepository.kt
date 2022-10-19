package com.pahnal.mystoryapp.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.pahnal.mystoryapp.data.vo.Resource
import com.pahnal.mystoryapp.domain.model.AddStory
import com.pahnal.mystoryapp.domain.model.Story
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface IStoryRepository {
    suspend fun addStory(file: MultipartBody.Part, addStory: AddStory,token: String?): Flow<Resource<String>>
    fun getAllStoriesRemoteMediator(token: String?): LiveData<PagingData<Story>>
    fun getAllStoriesFromDatabase(): LiveData<List<Story>>
    fun getStoryById(id: String): LiveData<Story>
}