package com.quiraadev.storyapp.ui.story

import com.quiraadev.storyapp.data.source.remote.response.StoryItem

sealed class StoryState {
    object Loading: StoryState()
    data class Error(val message : String) : StoryState()
    data class Success(val stories : List<StoryItem>) : StoryState()
}