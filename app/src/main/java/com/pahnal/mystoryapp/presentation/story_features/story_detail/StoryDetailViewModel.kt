package com.pahnal.mystoryapp.presentation.story_features.story_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pahnal.mystoryapp.domain.model.Story
import com.pahnal.mystoryapp.domain.repository.IStoryRepository

class StoryDetailViewModel(private val storyRepository: IStoryRepository) : ViewModel() {

    fun getStoryById(id: String): LiveData<Story> = storyRepository.getStoryById(id)
}