package com.pahnal.mystoryapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.pahnal.mystoryapp.data.mapper.toDomain
import com.pahnal.mystoryapp.data.source.local.room.StoryDatabase
import com.pahnal.mystoryapp.data.vo.Resource
import com.pahnal.mystoryapp.domain.model.AddStory
import com.pahnal.mystoryapp.domain.model.LoginRequest
import com.pahnal.mystoryapp.domain.model.RegisterRequest
import com.pahnal.mystoryapp.utils.DataDummy
import com.pahnal.mystoryapp.utils.MainDispatcherRule
import com.pahnal.mystoryapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MyRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private var apiService = FakeApiService()

    @Mock
    private lateinit var database: StoryDatabase

    @Test
    fun `When Login Should Success`() = runTest {
        val dummyLoginSuccess = DataDummy.generateDummyLoginSuccess()
        val loginRequest = LoginRequest(
            email = "arczel@gmail.com",
            password = "password",
        )

        val repository = MyRepository(database, apiService)
        val actualFlow = repository.login(loginRequest)

        Assert.assertNotNull(actualFlow)
        Assert.assertTrue(actualFlow.first() is Resource.Loading)
        Assert.assertTrue(actualFlow.last() is Resource.Success)
        Assert.assertEquals(dummyLoginSuccess.toDomain(), actualFlow.last().data)
        Assert.assertEquals(
            dummyLoginSuccess.toDomain().userId,
            (actualFlow.last() as Resource.Success).data?.userId
        )
    }

    @Test
    fun `When Register Should Success`() = runTest {
        val dummyRegisterSuccess = DataDummy.generateRegisterSuccess()
        val registerRequest = RegisterRequest(
            name = "arczel",
            email = "arczel@gmail.com",
            password = "password",
        )

        val repository = MyRepository(database, apiService)
        val actualFlow = repository.register(registerRequest)

        Assert.assertNotNull(actualFlow)
        Assert.assertTrue(actualFlow.first() is Resource.Loading)
        Assert.assertTrue(actualFlow.last() is Resource.Success)
        Assert.assertEquals(dummyRegisterSuccess.message, actualFlow.last().data)
    }

    @Test
    fun `When Add Story Should Success`() = runTest {
        val dummyAddStorySuccess = DataDummy.generateAddStorySuccess()
        val file = File("")
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo", file.name, requestImageFile
        )

        val addStory = AddStory(
            description = "",
            1.0,
            1.0,
        )

        val repository = MyRepository(database, apiService)
        val actualFlow = repository.addStory(
            file = imageMultipart,
            addStory,
            token = "",
        )

        Assert.assertNotNull(actualFlow)
        Assert.assertTrue(actualFlow.first() is Resource.Loading)
        Assert.assertTrue(actualFlow.last() is Resource.Success)
        Assert.assertEquals(
            dummyAddStorySuccess.message,
            (actualFlow.last() as Resource.Success).data
        )
    }

    @Test
    fun `When Get All Stories return List Stories`() = runTest {
        val storyDao = FakeStoryDao()
        storyDao.insertStory(DataDummy.generateDummyStories())

        val repository = MyRepository(database, apiService)
        Mockito.`when`(database.storyDao()).thenReturn(storyDao)
        val actualValue = repository.getAllStoriesFromDatabase()

        Assert.assertNotNull(actualValue)
        Assert.assertEquals(DataDummy.generateDummyStories(), actualValue.getOrAwaitValue())
        Assert.assertEquals(
            DataDummy.generateDummyStories().size,
            actualValue.getOrAwaitValue().size
        )
        Assert.assertEquals(
            DataDummy.generateDummyStories()[0].id,
            actualValue.getOrAwaitValue()[0].id
        )
    }

    @Test
    fun `When Get Story By Id Should return Single Story`() = runTest {
        val dummyStories = DataDummy.generateDummyStories()

        val storyDao = FakeStoryDao()
        storyDao.insertStory(dummyStories)

        val repository = MyRepository(database, apiService)
        Mockito.`when`(database.storyDao()).thenReturn(storyDao)
        val expectedValue = dummyStories[0]
        val actualValue = repository.getStoryById(expectedValue.id)

        Assert.assertNotNull(actualValue)
        Assert.assertEquals(expectedValue, actualValue.getOrAwaitValue())
        Assert.assertEquals(
            expectedValue.id,
            actualValue.getOrAwaitValue().id
        )
    }

}
