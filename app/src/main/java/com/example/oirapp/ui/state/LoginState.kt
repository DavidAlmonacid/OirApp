package com.example.oirapp.ui.state

sealed class LoginState {
    data class Success(val role: String?, val name: String?, val imageUrl: String?) : LoginState()
    data class Error(val message: String) : LoginState()
}
