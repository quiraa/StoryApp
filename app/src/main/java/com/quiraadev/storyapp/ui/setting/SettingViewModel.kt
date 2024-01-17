package com.quiraadev.storyapp.ui.setting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quiraadev.storyapp.data.preference.AppPreference
import kotlinx.coroutines.launch


class SettingViewModel : ViewModel() {

    private val _isLoggedIn = MutableLiveData(AppPreference.isLoggedIn)
    val isLoggedIn : LiveData<Boolean> = _isLoggedIn

    private val _isDarkMode = MutableLiveData(AppPreference.isDarkMode)
    val isDarkMode : LiveData<Boolean> = _isDarkMode

    fun logout() {
        AppPreference.token = ""
        AppPreference.isLoggedIn = false
        _isLoggedIn.postValue(false)
        Log.d("SettingViewModel", "Token: ${AppPreference.token}, IsLogin: ${_isLoggedIn.value}}")
    }

    fun setDarkMode(value : Boolean) {
        AppPreference.isDarkMode = value
        _isDarkMode.postValue(value)
    }

}