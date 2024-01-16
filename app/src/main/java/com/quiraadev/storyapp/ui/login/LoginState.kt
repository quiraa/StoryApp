package com.quiraadev.storyapp.ui.login

sealed class LoginState {
    object Loading : LoginState()
    data class Error(val message : String) : LoginState()
    object Success : LoginState()
}