package com.pahnal.mystoryapp.presentation.auth_features.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pahnal.mystoryapp.data.vo.Resource
import com.pahnal.mystoryapp.domain.model.LoginRequest
import com.pahnal.mystoryapp.domain.model.User
import com.pahnal.mystoryapp.domain.repository.IAuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: IAuthRepository
) : ViewModel() {

    private val _successLogin: MutableLiveData<User?> = MutableLiveData()
    val successLogin: LiveData<User?> = _successLogin

    private val _errorText: MutableLiveData<String> = MutableLiveData()
    val errorText: LiveData<String> = _errorText

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val data = repository.login(LoginRequest(email, password))
            data.collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _isLoading.value = true
                        _errorText.value = ""
                    }
                    is Resource.Error -> {
                        _isLoading.value = false
                        _errorText.value = result.message ?: "Error Message"
                        _successLogin.value = null
                    }
                    is Resource.Success -> {
                        _isLoading.value = false
                        _errorText.value = ""
                        _successLogin.value = result.data!!
                    }
                }
            }
        }
    }
}