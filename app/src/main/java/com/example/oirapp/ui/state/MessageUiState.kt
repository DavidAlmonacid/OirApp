package com.example.oirapp.ui.state

data class MessageUiState(
    val id: String = "",
    val senderId: String = "",
    val message: String = "",
    val timestamp: Long = 0L,
    val groupId: String = "",
)
