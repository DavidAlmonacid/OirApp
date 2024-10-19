package com.example.oirapp.ui.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oirapp.data.network.SupabaseClient.supabaseClient
import com.example.oirapp.ui.state.LoginState
import com.example.oirapp.ui.state.UserUiState
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.jsonPrimitive

class LoginViewModel : BaseViewModel() {
    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    private val _userUiState = MutableStateFlow(UserUiState())
    val userUiState: StateFlow<UserUiState> = _userUiState.asStateFlow()

    var userEmail by mutableStateOf("")
        private set

    var userPassword by mutableStateOf("")
        private set

    fun updateUserUiState(userUiState: UserUiState) {
        _userUiState.value = userUiState
    }

    fun updateUserEmail(email: String) {
        userEmail = email
    }

    fun updateUserPassword(password: String) {
        userPassword = password
    }

    private fun resetData() {
        userEmail = ""
        userPassword = ""
    }

    fun signInWithEmail(userEmail: String, userPassword: String) {
        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            _loginState.value = LoginState.Error("Ingrese los campos requeridos.")
            this.setShowDialog(true)
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            _loginState.value = LoginState.Error("Ingrese un correo electrónico válido.")
            this.setShowDialog(true)
            return
        }

        viewModelScope.launch {
            try {
                supabaseClient.auth.signInWith(Email) {
                    email = userEmail
                    password = userPassword
                }

                val user = supabaseClient.auth.currentSessionOrNull()?.user

                if (user != null) {
                    val userName = user.userMetadata?.get("nombre")?.jsonPrimitive?.content!!
                    val userRole = user.userMetadata?.get("rol")?.jsonPrimitive?.content!!
                    val userImageUrl = user.userMetadata?.get("imagen_url")?.jsonPrimitive?.content

                    _userUiState.value = UserUiState(
                        id = user.id,
                        name = userName,
                        role = userRole,
                        imageUrl = userImageUrl,
                    )
                }

                _loginState.value = LoginState.Success("Inicio de sesión exitoso.")
                resetData()
            } catch (e: Exception) {
                println("LoginViewModel.signInWithEmail: Error: ${e.message}")

                when (e.message) {
                    "Email not confirmed" -> {
                        _loginState.value = LoginState.Error("Confirme su correo electrónico.")
                    }

                    "Invalid login credentials" -> {
                        _loginState.value = LoginState.Error("Correo electrónico o contraseña incorrectos.")
                    }

                    else -> {
                        _loginState.value = LoginState.Error("Se produjo un error al iniciar sesión.")
                    }
                }

                this@LoginViewModel.setShowDialog(true)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _loginState.value = LoginState.Idle
            _userUiState.value = UserUiState()
            supabaseClient.auth.signOut()
        }
    }
}
