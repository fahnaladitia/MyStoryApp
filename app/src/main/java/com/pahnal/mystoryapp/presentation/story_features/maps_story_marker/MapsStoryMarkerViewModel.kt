package com.pahnal.mystoryapp.presentation.story_features.maps_story_marker

import androidx.lifecycle.ViewModel
import com.pahnal.mystoryapp.domain.repository.IStoryRepository

class MapsStoryMarkerViewModel(private val storyRepository: IStoryRepository): ViewModel() {

    fun getAllStories() = storyRepository.getAllStoriesFromDatabase()
}