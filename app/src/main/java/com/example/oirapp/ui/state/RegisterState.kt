package com.example.oirapp.ui.state

interface RegisterInterface {
    val message: String
}

sealed class RegisterState : RegisterInterface {
    data class Success(override val message: String) : RegisterState()
    data class Error(override val message: String) : RegisterState()
}
