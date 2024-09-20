package com.example.oirapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.oirapp.MainApplication

class NavigationViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    companion object {
        private const val CURRENT_SCREEN_KEY = "current_screen"
    }

    private val _currentScreen = savedStateHandle.getLiveData(CURRENT_SCREEN_KEY, MainApplication.Bienvenida)
    val currentScreen: LiveData<MainApplication> = _currentScreen

    fun updateCurrentScreen(screen: MainApplication) {
        _currentScreen.value = screen
        savedStateHandle[CURRENT_SCREEN_KEY] = screen
    }
}
