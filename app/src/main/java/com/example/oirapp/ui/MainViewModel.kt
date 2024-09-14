package com.example.oirapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oirapp.MainApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val _userUiState = MutableStateFlow(UserUiState())
    val userUiState: StateFlow<UserUiState> = _userUiState.asStateFlow()

    var userEmail by mutableStateOf("")
        private set

    fun updateUserEmail(email: String){
        userEmail = email
    }

    fun checkUserEmail() {
        TODO("Get the logic to allow a valid email")
    }

    private val _currentScreen = MutableLiveData(MainApplication.Bienvenida)
    val currentScreen: LiveData<MainApplication> = _currentScreen

    fun updateCurrentScreen(screen: MainApplication) {
        _currentScreen.value = screen
    }
}
