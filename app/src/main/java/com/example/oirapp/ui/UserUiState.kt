package com.example.oirapp.ui

data class UserUiState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val role: String = "",
    val isEmailWrong: Boolean = false,
)
