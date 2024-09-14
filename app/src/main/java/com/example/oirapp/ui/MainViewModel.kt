package com.example.oirapp.ui

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oirapp.MainApplication
import com.example.oirapp.data.network.SupabaseClient.supabaseClient
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class MainViewModel : ViewModel() {
    private val _userUiState = MutableStateFlow(UserUiState())
    val userUiState: StateFlow<UserUiState> = _userUiState.asStateFlow()

    // User email
    var userEmail by mutableStateOf("")
        private set

    fun updateUserEmail(email: String){
        userEmail = email
    }

    // User password
    var userPassword by mutableStateOf("")
        private set

    fun updateUserPassword(password: String){
        userPassword = password
    }

    // User name
    var userName by mutableStateOf("")
        private set

    fun updateUserName(name: String){
        userName = name
    }

    // User role
    var userRol by mutableStateOf("")
        private set

    fun updateUserRol(rol: String) {
        userRol = rol
    }

    fun createAccount(
        userEmail: String,
        userPassword: String,
        userName: String,
        userRol: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        if (userEmail.isEmpty() || userPassword.isEmpty() || userName.isEmpty()) {
            onError("Por favor, ingrese los campos requeridos.")
            return
        }

        if (userPassword.length < 6) {
            onError("La contraseña debe tener al menos 6 caracteres.")
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            onError("Por favor, ingrese un correo electrónico válido.")
            return
        }

        viewModelScope.launch {
            try {
                signUpNewUser(userEmail, userPassword, userName, userRol)
                onSuccess()
            } catch (e: Exception) {
                println("Error al crear la cuenta: ${e.message}")
                onError("Error al crear la cuenta: ${e.message}")
            }
        }
    }

    private suspend fun signUpNewUser(
        userEmail: String,
        userPassword: String,
        userName: String,
        userRol: String,
    ) {
        supabaseClient.auth.signUpWith(Email) {
            email = userEmail
            password = userPassword
            data = buildJsonObject {
                put("nombre", userName)
                put("rol", userRol)
                put("imagen_url", "")
            }
        }
    }

    private val _currentScreen = MutableLiveData(MainApplication.Bienvenida)
    val currentScreen: LiveData<MainApplication> = _currentScreen

    fun updateCurrentScreen(screen: MainApplication) {
        _currentScreen.value = screen
    }
}
