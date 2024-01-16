package com.quiraadev.storyapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quiraadev.storyapp.data.source.remote.retrofit.ApiService
import kotlinx.coroutines.launch


class StoryViewModel(
    private val api: ApiService
) : ViewModel() {
    private val _storyState = MutableLiveData<StoryState>(null)
    val storyState: LiveData<StoryState> = _storyState

    fun getAllStory() {
        viewModelScope.launch {
            _storyState.postValue(StoryState.Loading)
            try {
                val result = api.getAllStories()
                if (!result.error) {
                    _storyState.postValue(StoryState.Success(result.listStory))
                }
            } catch (e: Exception) {
                _storyState.postValue(StoryState.Error(e.message.toString()))
            }
        }
    }
}