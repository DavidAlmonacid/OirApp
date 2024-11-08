package com.example.oirapp.ui.viewmodel

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

    fun changeUserEmail(newEmail: String) {
        viewModelScope.launch {
            try {
                supabaseClient.auth.updateUser {
                    email = newEmail
                }

                _profileState.value = ProfileState.Success("Se ha enviado un correo de confirmación a $newEmail")
                this@MiPerfilViewModel.setShowDialog(true)
            } catch (e: Exception) {
                println("changeUserEmail: Error: ${e.message}")

                _profileState.value = ProfileState.Error("Error al cambiar el correo electrónico.")
                this@MiPerfilViewModel.setShowDialog(true)
            }
        }
    }

    fun changeUserName(newUserName: String) {
        viewModelScope.launch {
            try {
                supabaseClient.auth.updateUser {
                    data {
                        put("nombre", newUserName)
                    }
                }
                _profileState.value = ProfileState.Success("Nombre de usuario actualizado correctamente.")
            } catch (e: Exception) {
                println("changeUserName: Error: ${e.message}")

                _profileState.value = ProfileState.Error("Error al cambiar el nombre de usuario, intente nuevamente.")
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

                _profileState.value = ProfileState.Success("Imagen de perfil actualizada correctamente.")
            } catch (e: Exception) {
                println("uploadImageToSupabase: Error: ${e.message}")

                _profileState.value = ProfileState.Error("Error al cambiar la imagen de perfil, intente nuevamente.")
                this@MiPerfilViewModel.setShowDialog(true)
            }
        }
    }
}
