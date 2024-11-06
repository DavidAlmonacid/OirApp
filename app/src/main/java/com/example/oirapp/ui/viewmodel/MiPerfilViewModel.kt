package com.example.oirapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oirapp.data.network.SupabaseClient.supabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload
import io.ktor.http.encodeURLPath
import kotlinx.coroutines.launch
import kotlinx.serialization.json.put
import java.io.File

class MiPerfilViewModel : ViewModel() {
    private var imageUrl = ""

    fun updateUserImage(userName: String, imageFile: File, onImageUploaded: (String) -> Unit) {
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
                val uploadedImageUrl = supabaseClient.storage.from(bucketId).publicUrl(fileName).encodeURLPath()

                // Actualiza la URL de la imagen en la base de datos de Supabase
                supabaseClient.auth.updateUser {
                    data {
                        put("imagen_url", uploadedImageUrl)
                    }
                }

                // Actualiza la propiedad imageUrl
                imageUrl = uploadedImageUrl
                onImageUploaded(uploadedImageUrl)
            } catch (e: Exception) {
                println("uploadImageToSupabase: Error: ${e.message}")
            }
        }
    }
}
