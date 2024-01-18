package com.quiraadev.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quiraadev.storyapp.data.source.remote.retrofit.ApiService
import kotlinx.coroutines.launch


class RegisterViewModel(
    private val api: ApiService
) : ViewModel() {

    private val _registerState = MutableLiveData<RegisterState>(null)
    val registerState : LiveData<RegisterState> = _registerState

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _registerState.postValue(RegisterState.Loading)
            try {
                val result = api.registerUser(username, email, password)
                if(!result.error) {
                    _registerState.postValue(RegisterState.Success)
                }
            } catch (e : Exception) {
                _registerState.postValue(RegisterState.Error(e.message.toString()))
            }
        }
    }
}