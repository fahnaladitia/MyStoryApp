package com.pahnal.mystoryapp.presentation.story_features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pahnal.mystoryapp.domain.model.Story
import com.pahnal.mystoryapp.domain.repository.IStoryRepository

class HomeViewModel(private val storyRepository: IStoryRepository) : ViewModel() {

    fun listStories(token: String): LiveData<PagingData<Story>> =
        storyRepository.getAllStoriesPagingStory(token).cachedIn(viewModelScope)
}