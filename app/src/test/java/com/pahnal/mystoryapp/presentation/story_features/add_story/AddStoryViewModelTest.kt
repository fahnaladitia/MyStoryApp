package com.pahnal.mystoryapp.presentation.story_features.add_story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.pahnal.mystoryapp.data.repository.MyRepository
import com.pahnal.mystoryapp.data.vo.Resource
import com.pahnal.mystoryapp.domain.model.AddStory
import com.pahnal.mystoryapp.domain.repository.IStoryRepository
import com.pahnal.mystoryapp.utils.DataDummy
import com.pahnal.mystoryapp.utils.MainDispatcherRule
import com.pahnal.mystoryapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
class AddStoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var myRepository: MyRepository

    private val dummyAddStorySuccess = DataDummy.generateAddStorySuccess()
    private val dummyAddStoryError = DataDummy.generateAddStoryError()
    private val file = File("")
    private val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
    private val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
        "photo", file.name, requestImageFile
    )

    @Test
    fun `When Add Story Success, LiveData is in correct condition`() = runTest {
        val expectedFlow = flow {
            emit(Resource.Loading())
            emit(Resource.Success(dummyAddStorySuccess.message ?: ""))
        }
        Mockito.`when`(
            myRepository.addStory(
                imageMultipart,
                AddStory(
                    "",
                    0.0,
                    0.0,
                ),
                "",
            )
        ).thenReturn(expectedFlow)
        val storyRepository: IStoryRepository = myRepository
        val addStoryViewModel = AddStoryViewModel(storyRepository)
        addStoryViewModel.addStory(
            imageMultipart,
            AddStory(
                "",
                0.0,
                0.0,
            ),
            "",
        )
        Mockito.verify(myRepository).addStory(
            imageMultipart,
            AddStory(
                "",
                0.0,
                0.0,
            ),
            "",
        )
        Assert.assertFalse(addStoryViewModel.isLoading.getOrAwaitValue())
        Assert.assertEquals("", addStoryViewModel.errorText.getOrAwaitValue())
        Assert.assertTrue(addStoryViewModel.isSuccess.getOrAwaitValue())
    }

    @Test
    fun `When Add Story Error, LiveData is in correct condition`() = runTest {
        val expectedFlow = flow<Resource<String>> {
            emit(Resource.Loading())
            emit(Resource.Error(dummyAddStoryError.message ?: "Error message"))
        }
        Mockito.`when`(
            myRepository.addStory(
                imageMultipart,
                AddStory(
                    "",
                    0.0,
                    0.0,
                ),
                "",
            )
        ).thenReturn(expectedFlow)

        val storyRepository: IStoryRepository = myRepository
        val addStoryViewModel = AddStoryViewModel(storyRepository)
        addStoryViewModel.addStory(
            imageMultipart,
            AddStory(
                "",
                0.0,
                0.0,
            ),
            "",
        )
        Mockito.verify(myRepository).addStory(
            imageMultipart,
            AddStory(
                "",
                0.0,
                0.0,
            ),
            "",
        )
        Assert.assertFalse(addStoryViewModel.isLoading.getOrAwaitValue())
        Assert.assertNotNull(addStoryViewModel.errorText.getOrAwaitValue())
        Assert.assertEquals(
            (expectedFlow.last() as Resource.Error).message,
            addStoryViewModel.errorText.getOrAwaitValue()
        )
    }

}