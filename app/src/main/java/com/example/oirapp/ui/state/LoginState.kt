package com.example.oirapp.ui.state

interface LoginStateInterface {
    val message: String
}

sealed class LoginState : LoginStateInterface {
    data class Success(override val message: String) : LoginState()
    data class Error(override val message: String) : LoginState()
}
