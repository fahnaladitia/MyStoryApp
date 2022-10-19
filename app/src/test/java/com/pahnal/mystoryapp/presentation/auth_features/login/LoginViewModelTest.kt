package com.pahnal.mystoryapp.presentation.auth_features.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.pahnal.mystoryapp.data.mapper.toDomain
import com.pahnal.mystoryapp.data.repository.MyRepository
import com.pahnal.mystoryapp.data.vo.Resource
import com.pahnal.mystoryapp.domain.model.LoginRequest
import com.pahnal.mystoryapp.domain.model.User
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
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var myRepository: MyRepository

    private val dummyLoginSuccess = DataDummy.generateDummyLoginSuccess()
    private val dummyLoginErrorInvalidEmail = DataDummy.generateDummyLoginErrorInvalidEmail()
    private val dummyLoginErrorInvalidPassword = DataDummy.generateDummyLoginErrorInvalidPassword()
    private val dummyLoginErrorUserNotFound = DataDummy.generateDummyLoginErrorUserNotFound()
    private val email = "arczel@gmail.com"
    private val wrongEmailFormat = "arczelawgawg"
    private val password = "password"

    @Test
    fun `When Login Success, LiveData is in correct condition`() = runTest {
        val loginRequest = LoginRequest(
            email,
            password
        )

        val expectedFlow = flow {
            emit(Resource.Loading())
            emit(Resource.Success(dummyLoginSuccess.toDomain()))
        }
        Mockito.`when`(myRepository.login(loginRequest)).thenReturn(expectedFlow)

        val authRepository: IAuthRepository = myRepository
        val loginViewModel = LoginViewModel(authRepository)

        loginViewModel.login(email, password)
        Mockito.verify(myRepository).login(loginRequest)


        Assert.assertFalse(loginViewModel.isLoading.getOrAwaitValue())
        Assert.assertEquals("", loginViewModel.errorText.getOrAwaitValue())
        Assert.assertEquals(
            (expectedFlow.last() as Resource.Success).data,
            loginViewModel.successLogin.getOrAwaitValue()
        )
        Assert.assertEquals(
            (expectedFlow.last() as Resource.Success).data?.userId,
            loginViewModel.successLogin.getOrAwaitValue()?.userId
        )
    }

    @Test
    fun `When Login Error User Not Found, LiveData is in correct condition`() = runTest {
        val loginRequest = LoginRequest(
            email,
            password
        )

        val expectedFlow = flow<Resource<User>> {
            emit(Resource.Loading())
            emit(Resource.Error(dummyLoginErrorUserNotFound.message ?: "Error Message"))
        }
        Mockito.`when`(myRepository.login(loginRequest)).thenReturn(expectedFlow)

        val authRepository: IAuthRepository = myRepository
        val loginViewModel = LoginViewModel(authRepository)

        loginViewModel.login(email, password)
        Mockito.verify(myRepository).login(loginRequest)
        Assert.assertFalse(loginViewModel.isLoading.getOrAwaitValue())
        Assert.assertNotNull(loginViewModel.errorText.getOrAwaitValue())
        Assert.assertEquals(
            (expectedFlow.last() as Resource.Error).message,
            loginViewModel.errorText.getOrAwaitValue(),
        )

    }

    @Test
    fun `When Login Error Invalid Email, LiveData is in correct condition`() = runTest {
        val loginRequest = LoginRequest(
            wrongEmailFormat,
            password
        )

        val expectedFlow = flow<Resource<User>> {
            emit(Resource.Loading())
            emit(Resource.Error(dummyLoginErrorInvalidEmail.message ?: "Error Message"))
        }
        Mockito.`when`(myRepository.login(loginRequest)).thenReturn(expectedFlow)

        val authRepository: IAuthRepository = myRepository
        val loginViewModel = LoginViewModel(authRepository)

        loginViewModel.login(wrongEmailFormat, password)
        Mockito.verify(myRepository).login(loginRequest)

        Assert.assertFalse(loginViewModel.isLoading.getOrAwaitValue())
        Assert.assertNotNull(loginViewModel.errorText.getOrAwaitValue())
        Assert.assertEquals(
            (expectedFlow.last() as Resource.Error).message,
            loginViewModel.errorText.getOrAwaitValue(),
        )
    }

    @Test
    fun `When Login Error Invalid Password, LiveData is in correct condition`() = runTest {
        val loginRequest = LoginRequest(
            email,
            password
        )

        val expectedFlow = flow<Resource<User>> {
            emit(Resource.Loading())
            emit(Resource.Error(dummyLoginErrorInvalidPassword.message ?: "Error Message"))
        }
        Mockito.`when`(myRepository.login(loginRequest)).thenReturn(expectedFlow)

        val authRepository: IAuthRepository = myRepository
        val loginViewModel = LoginViewModel(authRepository)

        loginViewModel.login(email, password)
        Mockito.verify(myRepository).login(loginRequest)

        Assert.assertFalse(loginViewModel.isLoading.getOrAwaitValue())
        Assert.assertNotNull(loginViewModel.errorText.getOrAwaitValue())
        Assert.assertEquals(
            (expectedFlow.last() as Resource.Error).message,
            loginViewModel.errorText.getOrAwaitValue(),
        )
    }

}