package com.pahnal.mystoryapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pahnal.mystoryapp.data.di.Injection
import com.pahnal.mystoryapp.presentation.auth_features.login.LoginViewModel
import com.pahnal.mystoryapp.presentation.auth_features.register.RegisterViewModel
import com.pahnal.mystoryapp.presentation.story_features.add_story.AddStoryViewModel
import com.pahnal.mystoryapp.presentation.story_features.home.HomeViewModel

class ViewModelFactory :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(): ViewModelFactory {
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE = ViewModelFactory()
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val authRepository = Injection.provideAuthRepository()
        val storyRepository = Injection.provideIStoryRepository()
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(storyRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel : ${modelClass.name}")
        }
    }
}