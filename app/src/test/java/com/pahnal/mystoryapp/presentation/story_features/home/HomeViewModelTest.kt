package com.pahnal.mystoryapp.presentation.story_features.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.pahnal.mystoryapp.data.repository.MyRepository
import com.pahnal.mystoryapp.domain.model.Story
import com.pahnal.mystoryapp.domain.repository.IStoryRepository
import com.pahnal.mystoryapp.presentation.story_features.home.adapter.ListStoryAdapter
import com.pahnal.mystoryapp.utils.*
import kotlinx.coroutines.Dispatchers
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
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var myRepository: MyRepository
    private val dummyStory = DataDummy.generateDummyStories()



    @Test
    fun `When Get All Stories Should Not Null and Return Success`() = runTest {

        val data: PagingData<Story> = FakeStoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<Story>>()
        expectedStory.value = data
        Mockito.`when`(myRepository.getAllStoriesRemoteMediator("")).thenReturn(expectedStory)
        val storyRepository: IStoryRepository = myRepository
        val homeViewModel = HomeViewModel(storyRepository)

        val actualStory: PagingData<Story> = homeViewModel.listStories("").getOrAwaitValue()
        Mockito.verify(myRepository).getAllStoriesRemoteMediator("")
        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory, differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0].id, differ.snapshot().items[0].id)
    }

    @Test
    fun `When Get All Stories Return Empty`() = runTest {
        val data: PagingData<Story> = FakeStoryPagingSource.snapshot(listOf())
        val expectedStory = MutableLiveData<PagingData<Story>>()
        expectedStory.value = data
        Mockito.`when`(myRepository.getAllStoriesRemoteMediator("")).thenReturn(expectedStory)
        val storyRepository: IStoryRepository = myRepository
        val homeViewModel = HomeViewModel(storyRepository)

        val actualStory: PagingData<Story> = homeViewModel.listStories("").getOrAwaitValue()
        Mockito.verify(myRepository).getAllStoriesRemoteMediator("")
        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertEquals(emptyList<Story>(), differ.snapshot())
    }

}

