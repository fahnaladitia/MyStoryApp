package com.pahnal.mystoryapp.presentation.story_features.add_story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pahnal.mystoryapp.data.vo.Resource
import com.pahnal.mystoryapp.domain.model.AddStory
import com.pahnal.mystoryapp.domain.repository.IStoryRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class AddStoryViewModel(
    private val repository: IStoryRepository
) : ViewModel() {

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _errorText: MutableLiveData<String> = MutableLiveData()
    val errorText: LiveData<String> = _errorText

    fun addStory(file: MultipartBody.Part, addStory: AddStory, token: String) {
        viewModelScope.launch {
            repository.addStory(
                file, addStory, token
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _isLoading.value = true
                        _errorText.value = ""
                    }
                    is Resource.Error -> {
                        _isLoading.value = false
                        _errorText.value = result.message ?: "Error message"
                        _isSuccess.value = false
                    }
                    is Resource.Success -> {
                        _isLoading.value = false
                        _errorText.value = ""
                        _isSuccess.value = true
                    }
                }
            }

        }
    }
}