package com.quiraadev.storyapp.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quiraadev.storyapp.data.preference.AppPreference
import com.quiraadev.storyapp.data.source.remote.retrofit.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val api: ApiService
) : ViewModel() {

    private val _loginState = MutableLiveData<LoginState>(null)
    val loginState : LiveData<LoginState> = _loginState

    private val _isLoggedIn = MutableLiveData(AppPreference.isLoggedIn)
    val isLoggedIn : LiveData<Boolean> = _isLoggedIn

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.postValue(LoginState.Loading)
            try {
                val result = api.loginUser(email, password)
                if(!result.error) {
                    _loginState.postValue(LoginState.Success)
                }
                AppPreference.token = result.loginResult.token
                saveSession(true)
                Log.d("LoginViewModel", "Token: ${result.loginResult.token}, IsLogin: ${_isLoggedIn.value}")
            } catch (e : Exception) {
                _loginState.postValue(
                    LoginState.Error(e.message.toString())
                )
            }
        }
    }

    private fun saveSession(value : Boolean) {
        viewModelScope.launch {
            AppPreference.isLoggedIn = value
            _isLoggedIn.postValue(value)
        }
    }
}