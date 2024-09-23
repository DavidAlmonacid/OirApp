package com.example.oirapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.oirapp.data.network.SupabaseClient.supabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class GruposViewModel : BaseViewModel() {
    var userInput by mutableStateOf("")
        private set

    fun updateUserInput(input: String) {
        userInput = input
    }

    fun resetData() {
        userInput = ""
    }

    fun createGroup(groupName: String, idDocente: String) {
        viewModelScope.launch {
            try {
                val table = supabaseClient.postgrest["grupos"]

                table.insert(buildJsonObject {
                    put("nombre_grupo", groupName)
                    put("codigo_acceso", generateAccessCode())
                    put("id_docente", idDocente)
                })

                resetData()
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }
    }

    private fun generateAccessCode(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val randomLimit = (5..chars.length).random()

        val charsArray = chars.toCharArray()
        charsArray.shuffle()

        return String(charsArray.sliceArray((randomLimit - 5)..randomLimit))
    }
}
