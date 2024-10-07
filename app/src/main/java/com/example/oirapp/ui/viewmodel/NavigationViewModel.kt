package com.example.oirapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.oirapp.MainApplication

class NavigationViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    companion object {
        private const val CURRENT_SCREEN_KEY = "current_screen"
    }

    private val _currentScreen = savedStateHandle.getLiveData(CURRENT_SCREEN_KEY, MainApplication.Bienvenida)
    val currentScreen: LiveData<MainApplication> = _currentScreen

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> get() = _title

    fun updateCurrentScreen(screen: MainApplication) {
        _currentScreen.value = screen
        savedStateHandle[CURRENT_SCREEN_KEY] = screen
    }

    fun updateTitle(newTitle: String) {
        _title.value = newTitle
    }
}
