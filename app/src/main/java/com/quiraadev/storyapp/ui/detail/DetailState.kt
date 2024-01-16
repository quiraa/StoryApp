package com.quiraadev.storyapp.ui.detail

import com.quiraadev.storyapp.data.source.remote.response.StoryItem

sealed class DetailState {
    object Loading : DetailState()
    data class Error(val message : String) : DetailState()
    data class Success(val story: StoryItem) : DetailState()
}