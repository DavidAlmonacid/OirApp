package com.example.oirapp.ui.state

sealed class LoginState<out T> {
    data class Success<T>(val data: T) : LoginState<T>()
    data class Error(val message: String) : LoginState<Nothing>()
}
