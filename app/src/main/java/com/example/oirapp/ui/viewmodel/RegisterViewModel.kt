package com.example.oirapp.ui.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oirapp.data.network.SupabaseClient.supabaseClient
import com.example.oirapp.ui.state.RegisterState
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class RegisterViewModel : BaseViewModel() {
    private val _registerState = MutableLiveData<RegisterState>()
    val registerState: LiveData<RegisterState> = _registerState

    var userEmail by mutableStateOf("")
        private set

    var userPassword by mutableStateOf("")
        private set

    var userName by mutableStateOf("")
        private set

    var userRole by mutableStateOf("")
        private set

    fun updateUserEmail(email: String) {
        userEmail = email
    }

    fun updateUserPassword(password: String) {
        userPassword = password
    }

    fun updateUserName(name: String) {
        userName = name
    }

    fun updateUserRole(role: String) {
        userRole = role
    }

    fun createAccount(
        userEmail: String,
        userPassword: String,
        userName: String,
        userRole: String,
    ) {
        if (userEmail.isEmpty() || userPassword.isEmpty() || userName.isEmpty() || userRole.isEmpty()) {
            _registerState.value = RegisterState.Error("Por favor, ingrese los campos requeridos.")
            this.setShowErrorDialog(true)
            return
        }

        if (userPassword.length < 6) {
            _registerState.value = RegisterState.Error("La contraseña debe tener al menos 6 caracteres.")
            this.setShowErrorDialog(true)
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            _registerState.value = RegisterState.Error("Por favor, ingrese un correo electrónico válido.")
            this.setShowErrorDialog(true)
            return
        }

        viewModelScope.launch {
            try {
                signUpNewUser(
                    userEmail = userEmail,
                    userPassword = userPassword,
                    userName = userName,
                    userRole = userRole,
                )
                _registerState.value = RegisterState.Success("Se ha enviado un correo de verificación a $userEmail")
                this@RegisterViewModel.setShowSuccessDialog(true)
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error("Error al crear la cuenta: ${e.message}")
                this@RegisterViewModel.setShowErrorDialog(true)
            }
        }
    }

    private suspend fun signUpNewUser(
        userEmail: String,
        userPassword: String,
        userName: String,
        userRole: String,
    ) {
        supabaseClient.auth.signUpWith(Email) {
            email = userEmail
            password = userPassword
            data = buildJsonObject {
                put("nombre", userName)
                put("rol", userRole)
                put("imagen_url", "")
            }
        }
    }

    fun resetData() {
        userEmail = ""
        userPassword = ""
        userName = ""
        userRole = ""
    }
}

/*
 * TODO: Si algo fue mal, mostrar mensaje de error en un AlertDialog
 */
