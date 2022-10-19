package com.pahnal.mystoryapp.presentation.story_features.story_detail

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
class StoryDetailViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var myRepository: MyRepository


    @Test
    fun `When Get Story By Id return Single Story`() = runTest {
        val dummyStory = DataDummy.generateDummyStories()[0]

        val data = MutableLiveData<Story>()
        data.value = dummyStory

        Mockito.`when`(myRepository.getStoryById(dummyStory.id)).thenReturn(data)

        val storyRepository: IStoryRepository = myRepository
        val storyDetailViewModel = StoryDetailViewModel(storyRepository)

        val actualValue = storyDetailViewModel.getStoryById(dummyStory.id)

        Assert.assertNotNull(actualValue)
        Assert.assertEquals(dummyStory, actualValue.getOrAwaitValue())
        Assert.assertEquals(dummyStory.id, actualValue.getOrAwaitValue().id)
    }
}