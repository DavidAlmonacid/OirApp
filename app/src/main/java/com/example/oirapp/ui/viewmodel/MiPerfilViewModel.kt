package com.example.oirapp.ui.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.oirapp.data.network.SupabaseClient.supabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload
import io.ktor.http.encodeURLPath
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.put
import java.io.File

sealed interface ProfileState {
    data object Idle : ProfileState
    data class Success(val message: String) : ProfileState
    data class Error(val message: String) : ProfileState
}

class MiPerfilViewModel : BaseViewModel() {
    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    var userInputEmail by mutableStateOf("")
        private set

    var userInputPassword by mutableStateOf("")
        private set

    var userInputName by mutableStateOf("")
        private set

    fun updateUserInputEmail(inputEmail: String) {
        userInputEmail = inputEmail
    }

    fun updateUserInputPassword(inputPassword: String) {
        userInputPassword = inputPassword
    }

    fun updateUserInputName(inputName: String) {
        userInputName = inputName
    }

    fun changeUserEmail(newEmail: String) {
        if (newEmail.isBlank()) {
            userInputEmail = ""
            _profileState.value = ProfileState.Error("El correo electrónico no puede estar vacío.")
            this.setShowDialog(true)
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            userInputEmail = ""
            _profileState.value = ProfileState.Error("El correo electrónico no es válido.")
            this.setShowDialog(true)
            return
        }

        viewModelScope.launch {
            try {
                supabaseClient.auth.updateUser {
                    email = newEmail
                }

                _profileState.value =
                    ProfileState.Success("Se ha enviado un correo de confirmación a $newEmail")
                this@MiPerfilViewModel.setShowDialog(true)
            } catch (e: Exception) {
                println("changeUserEmail: Error: ${e.message}")

                _profileState.value = ProfileState.Error("Error al cambiar el correo electrónico.")
                this@MiPerfilViewModel.setShowDialog(true)
            }
        }
    }

    fun changeUserPassword(newPassword: String) {
        if (newPassword.isBlank()) {
            userInputPassword = ""
            _profileState.value = ProfileState.Error("La contraseña no puede estar vacía.")
            this.setShowDialog(true)
            return
        }

        if (newPassword.length < 6) {
            userInputPassword = ""
            _profileState.value =
                ProfileState.Error("La contraseña debe tener al menos 6 caracteres.")
            this.setShowDialog(true)
            return
        }

        viewModelScope.launch {
            try {
                supabaseClient.auth.updateUser {
                    password = newPassword
                }

                _profileState.value = ProfileState.Success("Contraseña actualizada correctamente.")
                this@MiPerfilViewModel.setShowDialog(true)
            } catch (e: Exception) {
                println("changeUserPassword: Error: ${e.message}")

                when (e.message) {
                    "New password should be different from the old password." -> {
                        _profileState.value =
                            ProfileState.Error("La nueva contraseña debe ser diferente a la anterior.")
                    }

                    else -> {
                        _profileState.value =
                            ProfileState.Error("Error al cambiar la contraseña, intente nuevamente.")
                    }
                }

                this@MiPerfilViewModel.setShowDialog(true)
            } finally {
                userInputPassword = ""
            }
        }
    }

    fun changeUserName(newUserName: String) {
        if (newUserName.isBlank()) {
            userInputName = ""
            _profileState.value = ProfileState.Error("El nombre de usuario no puede estar vacío.")
            this.setShowDialog(true)
            return
        }

        viewModelScope.launch {
            try {
                supabaseClient.auth.updateUser {
                    data {
                        put("nombre", newUserName)
                    }
                }
                _profileState.value =
                    ProfileState.Success("Nombre de usuario actualizado correctamente.")
            } catch (e: Exception) {
                println("changeUserName: Error: ${e.message}")

                _profileState.value =
                    ProfileState.Error("Error al cambiar el nombre de usuario, intente nuevamente.")
                this@MiPerfilViewModel.setShowDialog(true)
            }
        }
    }

    private var imageUrl = ""

    fun changeUserImage(
        userName: String,
        imageFile: File,
        onImageUploaded: (String) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                val bucketId = "profile_images"

                // Sube la imagen a Supabase Storage
                val bucket = supabaseClient.storage.from(bucketId)
                val fileName = "${userName}_${System.currentTimeMillis()}.jpg"

                bucket.upload(fileName, imageFile) {
                    upsert = false
                }

                // Obtiene la URL de la imagen subida
                val uploadedImageUrl =
                    supabaseClient.storage.from(bucketId).publicUrl(fileName).encodeURLPath()

                // Actualiza la URL de la imagen en la base de datos de Supabase
                supabaseClient.auth.updateUser {
                    data {
                        put("imagen_url", uploadedImageUrl)
                    }
                }

                // Actualiza la propiedad imageUrl
                imageUrl = uploadedImageUrl
                onImageUploaded(uploadedImageUrl)

                _profileState.value =
                    ProfileState.Success("Imagen de perfil actualizada correctamente.")
            } catch (e: Exception) {
                println("uploadImageToSupabase: Error: ${e.message}")

                _profileState.value =
                    ProfileState.Error("Error al cambiar la imagen de perfil, intente nuevamente.")
                this@MiPerfilViewModel.setShowDialog(true)
            }
        }
    }
}
