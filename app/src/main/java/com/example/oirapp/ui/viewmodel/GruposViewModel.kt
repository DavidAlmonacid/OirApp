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

    fun createGroup(groupName: String, idDocente: String) {
        viewModelScope.launch {
            createGroupSuspend(
                groupName = groupName,
                accessCode = generateAccessCode(),
                idDocente = idDocente,
            )
        }
    }

    private suspend fun createGroupSuspend(
        groupName: String,
        accessCode: String,
        idDocente: String,
    ) {
        val table = supabaseClient.postgrest["grupos"]

        table.insert(buildJsonObject {
            put("nombre_grupo", groupName)
            put("codigo_acceso", accessCode)
            put("id_docente", idDocente)
        })
    }

    private fun generateAccessCode(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val randomLimit = (5..chars.length).random()

        val charsArray = chars.toCharArray()
        charsArray.shuffle()

        return String(charsArray.sliceArray((randomLimit - 5)..randomLimit))
    }
}
