package com.quiraadev.storyapp.ui.register

sealed class RegisterState {
    object Loading: RegisterState()
    data class Error(val message : String) : RegisterState()
    object Success : RegisterState()
}