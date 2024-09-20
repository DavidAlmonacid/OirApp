package com.example.oirapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oirapp.MainApplication

class NavigationViewModel : ViewModel() {
    private val _currentScreen = MutableLiveData(MainApplication.Bienvenida)
    val currentScreen: LiveData<MainApplication> = _currentScreen

    fun updateCurrentScreen(screen: MainApplication) {
        _currentScreen.value = screen
    }
}
