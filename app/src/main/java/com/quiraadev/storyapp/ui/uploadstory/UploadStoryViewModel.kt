package com.quiraadev.storyapp.ui.uploadstory

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.quiraadev.storyapp.data.source.remote.retrofit.ApiService
import com.quiraadev.storyapp.utils.buildImageBodyPart
import com.quiraadev.storyapp.utils.convertToFile
import com.quiraadev.storyapp.utils.setRequestBody
import kotlinx.coroutines.launch

class UploadStoryViewModel(
    private val api: ApiService,
    application: Application
) : AndroidViewModel(application) {

    val context = application.applicationContext

    private val _uploadState = MutableLiveData<UploadStoryState>(null)
    val uploadState: LiveData<UploadStoryState> = _uploadState

    private val _image = MutableLiveData<Bitmap>(null)
    val image: LiveData<Bitmap> = _image

    fun setImageBitmap(newImage: Bitmap) = _image.postValue(newImage)

    fun uploadStory(description: String, lat: Float?, long: Float?) {
        val file = _image.value?.convertToFile(context, description)
        viewModelScope.launch {
            _uploadState.postValue(UploadStoryState.Loading)
            try {
                val response = api.uploadStory(
                    description.setRequestBody(),
                    file?.buildImageBodyPart(),
                    lat,
                    long
                )

                if (!response.error) {
                    _uploadState.postValue(UploadStoryState.Success)
                }
            } catch (e: Exception) {
                _uploadState.postValue(UploadStoryState.Error(e.message.toString()))
            }
        }
    }
}