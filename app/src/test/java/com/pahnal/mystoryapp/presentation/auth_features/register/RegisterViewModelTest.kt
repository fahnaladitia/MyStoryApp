package com.pahnal.mystoryapp.presentation.auth_features.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.pahnal.mystoryapp.data.repository.MyRepository
import com.pahnal.mystoryapp.data.vo.Resource
import com.pahnal.mystoryapp.domain.model.RegisterRequest
import com.pahnal.mystoryapp.domain.repository.IAuthRepository
import com.pahnal.mystoryapp.utils.DataDummy
import com.pahnal.mystoryapp.utils.MainDispatcherRule
import com.pahnal.mystoryapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
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
class RegisterViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var myRepository: MyRepository

    private val dummyRegisterSuccess = DataDummy.generateRegisterSuccess()
    private val dummyRegisterErrorInvalidEmail = DataDummy.generateRegisterErrorInvalidEmail()
    private val dummyRegisterErrorEmailAlreadyTaken = DataDummy.generateRegisterErrorEmailAlreadyTaken()
    private val name = "Arczel"
    private val email = "arczel@gmail.com"
    private val wrongEmailFormat = "arczelawgawg"
    private val password = "password"

    @Test
    fun `When Register Success, LiveData is in correct condition`() = runTest {

        val registerRequest = RegisterRequest(name, email, password)
        val expectedFlow = flow {
            emit(Resource.Loading())
            emit(Resource.Success(dummyRegisterSuccess.message ?: ""))
        }
        Mockito.`when`(myRepository.register(registerRequest)).thenReturn(expectedFlow)

        val authRepository: IAuthRepository = myRepository
        val registerViewModel = RegisterViewModel(authRepository)

        registerViewModel.register(
            name, email, password
        )
        Mockito.verify(myRepository).register(registerRequest)

        Assert.assertFalse(registerViewModel.isLoading.getOrAwaitValue())
        Assert.assertEquals("", registerViewModel.errorText.getOrAwaitValue())
        Assert.assertEquals(
            (expectedFlow.last() as Resource.Success).data,
            registerViewModel.successText.getOrAwaitValue(),
        )
    }

    @Test
    fun `When Register Error Invalid Email, LiveData is in correct condition`() = runTest {
        val registerRequest = RegisterRequest(name, wrongEmailFormat, password)

        val expectedFlow = flow<Resource<String>> {
            emit(Resource.Loading())
            emit(Resource.Error(dummyRegisterErrorInvalidEmail.message ?: ""))
        }

        Mockito.`when`(myRepository.register(registerRequest)).thenReturn(expectedFlow)

        val authRepository: IAuthRepository = myRepository
        val registerViewModel = RegisterViewModel(authRepository)

        registerViewModel.register(name, wrongEmailFormat, password)
        Mockito.verify(myRepository).register(registerRequest)

        Assert.assertFalse(registerViewModel.isLoading.getOrAwaitValue())
        Assert.assertNotNull(registerViewModel.errorText.getOrAwaitValue())
        Assert.assertEquals(
            (expectedFlow.last() as Resource.Error).message,
            registerViewModel.errorText.getOrAwaitValue(),
        )
    }

    @Test
    fun `When Register Error Email Already Taken, LiveData is in correct condition`() = runTest {
        val registerRequest = RegisterRequest(name, wrongEmailFormat, password)

        val expectedFlow = flow<Resource<String>> {
            emit(Resource.Loading())
            emit(Resource.Error(dummyRegisterErrorEmailAlreadyTaken.message ?: ""))
        }

        Mockito.`when`(myRepository.register(registerRequest)).thenReturn(expectedFlow)

        val authRepository: IAuthRepository = myRepository
        val registerViewModel = RegisterViewModel(authRepository)

        registerViewModel.register(name, wrongEmailFormat, password)
        Mockito.verify(myRepository).register(registerRequest)

        Assert.assertFalse(registerViewModel.isLoading.getOrAwaitValue())
        Assert.assertNotNull(registerViewModel.errorText.getOrAwaitValue())
        Assert.assertEquals(
            (expectedFlow.last() as Resource.Error).message,
            registerViewModel.errorText.getOrAwaitValue(),
        )
    }
}