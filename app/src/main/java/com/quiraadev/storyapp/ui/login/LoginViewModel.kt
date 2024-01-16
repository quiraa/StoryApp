package com.quiraadev.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quiraadev.storyapp.data.preference.AppPreference
import com.quiraadev.storyapp.data.source.remote.retrofit.ApiService
import kotlinx.coroutines.launch

class LoginViewModel(
    private val api: ApiService
) : ViewModel() {

    private val _loginState = MutableLiveData<LoginState>(null)
    val loginState : LiveData<LoginState> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.postValue(LoginState.Loading)
            try {
                val result = api.loginUser(email, password)
                if(!result.error) {
                    _loginState.postValue(LoginState.Success)
                }
                AppPreference.token = result.loginResult.token
            } catch (e : Exception) {
                _loginState.postValue(
                    LoginState.Error(e.message.toString())
                )
            }
        }
    }
}