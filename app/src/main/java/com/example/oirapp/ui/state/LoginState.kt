package com.example.oirapp.ui.state

sealed class LoginState {
    data class Success(val message: String) : LoginState()
    data class Error(val message: String) : LoginState()
    data object Idle : LoginState()
}
