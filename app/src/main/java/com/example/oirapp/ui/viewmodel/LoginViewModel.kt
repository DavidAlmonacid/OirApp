package com.example.oirapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oirapp.data.network.SupabaseClient.supabaseClient
import com.example.oirapp.ui.state.LoginState
import com.example.oirapp.ui.state.UserUiState
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
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
            _loginState.value = LoginState.Error("Por favor, ingrese los campos requeridos.")
            return
        }

        viewModelScope.launch {
            try {
                supabaseClient.auth.signInWith(Email) {
                    email = userEmail
                    password = userPassword
                }

                val user = supabaseClient.auth.currentSessionOrNull()?.user
                val userRole = user?.userMetadata?.get("rol")?.jsonPrimitive?.content
                val userName = user?.userMetadata?.get("nombre")?.jsonPrimitive?.content
                val userImageUrl = user?.userMetadata?.get("imagen_url")?.jsonPrimitive?.content

                _userUiState.value = UserUiState(
                    id = user?.id ?: "",
                    name = userName ?: "",
                    role = userRole ?: "",
                    imageUrl = userImageUrl ?: "",
                )

                _loginState.value = LoginState.Success("Inicio de sesión exitoso.")
                resetData()
            } catch (e: Exception) {
                when (e.message) {
                    "Email not confirmed" -> {
                        _loginState.value =
                            LoginState.Error("Por favor, confirme su correo electrónico.")
                    }

                    else -> {
                        _loginState.value =
                            LoginState.Error("Se produjo un error al iniciar sesión.")
                    }
                }

                println("Error: ${e.message}")
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

/*
 * TODO: Si algo fue mal, mostrar mensaje de error en un AlertDialog
 */
