package com.quiraadev.storyapp.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class DetailResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("story")
    val story: StoryItem
)
