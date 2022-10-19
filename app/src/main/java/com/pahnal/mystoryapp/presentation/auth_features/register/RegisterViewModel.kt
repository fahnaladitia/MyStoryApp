package com.pahnal.mystoryapp.presentation.auth_features.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pahnal.mystoryapp.data.vo.Resource
import com.pahnal.mystoryapp.domain.model.RegisterRequest
import com.pahnal.mystoryapp.domain.repository.IAuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: IAuthRepository) : ViewModel() {

    private val _successText: MutableLiveData<String> = MutableLiveData()
    val successText: LiveData<String> = _successText

    private val _errorText: MutableLiveData<String> = MutableLiveData()
    val errorText: LiveData<String> = _errorText

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(
        name: String,
        email: String,
        password: String,
    ) {
        viewModelScope.launch {
            repository.register(RegisterRequest(name, email, password))
                .collect { result ->
                    when(result){
                        is Resource.Loading -> {
                            _isLoading.value = true
                            _errorText.value = ""
                        }
                        is Resource.Error -> {
                            _isLoading.value = false
                            _errorText.value = result.message ?: "Error Message"
                            _successText.value = ""
                        }
                        is Resource.Success -> {
                            _isLoading.value = false
                            _errorText.value = ""
                            _successText.value = result.data ?: "Success"
                        }
                    }
                }
        }
    }
}