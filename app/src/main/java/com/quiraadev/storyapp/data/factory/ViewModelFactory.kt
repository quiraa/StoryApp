package com.quiraadev.storyapp.data.factory

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.quiraadev.storyapp.data.source.remote.retrofit.ApiService
import com.quiraadev.storyapp.di.Injection
import com.quiraadev.storyapp.ui.detail.DetailViewModel
import com.quiraadev.storyapp.ui.login.LoginViewModel
import com.quiraadev.storyapp.ui.register.RegisterViewModel
import com.quiraadev.storyapp.ui.story.StoryViewModel
import com.quiraadev.storyapp.ui.uploadstory.UploadStoryViewModel

class ViewModelFactory(private val api: ApiService, private val context: Application) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(api) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(api) as T
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> StoryViewModel(api) as T
            modelClass.isAssignableFrom(UploadStoryViewModel::class.java) -> UploadStoryViewModel(api, context) as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> DetailViewModel(api) as T
            else -> throw IllegalArgumentException("Unknown ViewModel: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Application): ViewModelFactory {
            return INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(Injection.provideApiService(), context).also {
                    INSTANCE = it
                }
            }
        }
    }
}