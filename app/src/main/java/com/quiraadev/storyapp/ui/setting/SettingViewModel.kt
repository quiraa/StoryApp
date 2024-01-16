package com.quiraadev.storyapp.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quiraadev.storyapp.data.preference.AppPreference
import kotlinx.coroutines.launch


class SettingViewModel : ViewModel() {

    private val _isLoggedIn = MutableLiveData(AppPreference.isLoggedIn)
    val isLoggedIn : LiveData<Boolean> = _isLoggedIn

    fun logout() {
        viewModelScope.launch {
            clearSession()
        }
    }

    private fun clearSession() {
        AppPreference.token = ""
        AppPreference.isLoggedIn = false
        _isLoggedIn.postValue(false)
    }

}