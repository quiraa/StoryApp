package com.quiraadev.storyapp.ui.uploadstory

sealed class UploadStoryState {
    object Loading : UploadStoryState()
    data class Error(val message: String) : UploadStoryState()
    object Success : UploadStoryState()
}