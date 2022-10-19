package com.pahnal.mystoryapp.presentation.story_features.maps_story_marker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.pahnal.mystoryapp.data.repository.MyRepository
import com.pahnal.mystoryapp.domain.model.Story
import com.pahnal.mystoryapp.domain.repository.IStoryRepository
import com.pahnal.mystoryapp.utils.DataDummy
import com.pahnal.mystoryapp.utils.MainDispatcherRule
import com.pahnal.mystoryapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsStoryMarkerViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var myRepository: MyRepository

    @Test
    fun `When Get All Stories from Database must be return List Story`() = runTest {
        val dummyStories = DataDummy.generateDummyStories()

        val data = MutableLiveData<List<Story>>()
        data.value = dummyStories

        Mockito.`when`(myRepository.getAllStoriesFromDatabase()).thenReturn(data)

        val storyRepository: IStoryRepository = myRepository
        val mapsStoryMarkerViewModel = MapsStoryMarkerViewModel(storyRepository)
        val actualValue = mapsStoryMarkerViewModel.getAllStories()

        Mockito.verify(myRepository).getAllStoriesFromDatabase()

        Assert.assertNotNull(actualValue)
        Assert.assertEquals(dummyStories.size,actualValue.getOrAwaitValue().size)
        Assert.assertEquals(dummyStories[0].id,actualValue.getOrAwaitValue()[0].id)
    }

}