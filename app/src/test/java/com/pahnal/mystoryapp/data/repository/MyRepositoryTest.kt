package com.pahnal.mystoryapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.pahnal.mystoryapp.data.mapper.toDomain
import com.pahnal.mystoryapp.data.mapper.toDto
import com.pahnal.mystoryapp.data.source.remote.network.ApiService
import com.pahnal.mystoryapp.data.vo.Resource
import com.pahnal.mystoryapp.domain.model.AddStory
import com.pahnal.mystoryapp.domain.model.LoginRequest
import com.pahnal.mystoryapp.domain.model.RegisterRequest
import com.pahnal.mystoryapp.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

@ExperimentalCoroutinesApi
class MyRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var apiService: ApiService
    private lateinit var repository: MyRepository

    @Before
    fun setup() {
        apiService = FakeApiService()
        repository = MyRepository(apiService)
    }

    @Test
    fun `When Login Should Success`() = runTest {
        val loginRequest = LoginRequest(
            email = "arczel@gmail.com",
            password = "password",
        )

        val expectedFlow = flow {
            emit(Resource.Loading())
            emit(Resource.Success(apiService.login(loginRequest.toDto()).toDomain()))
        }

        val actualFlow = repository.login(loginRequest)

        Assert.assertNotNull(actualFlow)
        Assert.assertTrue(actualFlow.first() is Resource.Loading)
        Assert.assertTrue(actualFlow.last() is Resource.Success)
        Assert.assertEquals(expectedFlow.last().data, actualFlow.last().data)
        Assert.assertEquals(
            expectedFlow.last().data?.userId, (actualFlow.last() as Resource.Success).data?.userId
        )
    }

    @Test
    fun `When Register Should Success`() = runTest {
        val registerRequest = RegisterRequest(
            name = "arczel",
            email = "arczel@gmail.com",
            password = "password",
        )

        val expectedFlow = flow {
            emit(Resource.Loading())
            emit(
                Resource.Success(
                    apiService.register(registerRequest.toDto()).message ?: "Success"
                )
            )
        }

        val actualFlow = repository.register(registerRequest)

        Assert.assertNotNull(actualFlow)
        Assert.assertTrue(actualFlow.first() is Resource.Loading)
        Assert.assertTrue(actualFlow.last() is Resource.Success)
        Assert.assertEquals(expectedFlow.last().data, actualFlow.last().data)
    }

    @Test
    fun `When get All Stories Pagination Should Success`() = runTest {

    }

    @Test
    fun `When Add Story Should Success`() = runTest {
        val file = File("")
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo", file.name, requestImageFile
        )

        val expectedFlow = flow {
            emit(Resource.Loading())
            emit(
                Resource.Success(
                    apiService.addNewStory(
                        file = imageMultipart,
                        description = "".toRequestBody("text/plain".toMediaType()),
                        lat = 1.0.toString().toRequestBody("text/plain".toMediaType()),
                        lon = 1.0.toString().toRequestBody("text/plain".toMediaType()),
                        token = "",
                    ).message ?: "Success",
                ),
            )
        }

        val actualFlow = repository.addStory(
            imageMultipart,
            AddStory(
                description = "", 1.0, 1.0
            ),
            "",
        )

        Assert.assertNotNull(actualFlow)
        Assert.assertTrue(actualFlow.first() is Resource.Loading)
        Assert.assertTrue(actualFlow.last() is Resource.Success)
        Assert.assertEquals(expectedFlow.last().data, actualFlow.last().data)
    }

}
