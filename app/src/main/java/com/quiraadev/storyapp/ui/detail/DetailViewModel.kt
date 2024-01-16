package com.quiraadev.storyapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quiraadev.storyapp.data.source.remote.retrofit.ApiService
import kotlinx.coroutines.launch

class DetailViewModel(
    private val api: ApiService
) : ViewModel() {

    private val _detailState = MutableLiveData<DetailState>(null)
    val detailState: LiveData<DetailState> = _detailState

    fun getDetailStory(storyId: String) {
        viewModelScope.launch {
            _detailState.postValue(DetailState.Loading)
            try {
                val result = api.getDetailStory(storyId)
                _detailState.postValue(
                    DetailState.Success(result.story)
                )
            } catch (e: Exception) {
                _detailState.postValue(DetailState.Error(e.message.toString()))
            }
        }
    }
}