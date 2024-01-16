package com.quiraadev.storyapp.data.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class StoryResponse(
    @SerializedName("listStory")
    val listStory : List<StoryItem>,

    @SerializedName("error")
    val error : Boolean,

    @SerializedName("message")
    val message : String
)

@Parcelize
data class StoryItem(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("createdAt")
    val createdAt : String,

    @SerializedName("photoUrl")
    val photoUrl: String,

    @SerializedName("lon")
    val lon: Double,

    @SerializedName("lat")
    val lat: Double
) : Parcelable