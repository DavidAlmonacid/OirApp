package com.example.oirapp.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.oirapp.data.network.SupabaseClient.supabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class GruposViewModel : BaseViewModel() {
    fun createGroup(groupName: String, idDocente: String) {
        val accessCode = generateAccessCode()
        viewModelScope.launch {
            createGroupSuspend(groupName, accessCode, idDocente)
        }
    }

    private suspend fun createGroupSuspend(groupName: String, accessCode: String, idDocente: String) {
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

        val code = String(charsArray.sliceArray((randomLimit - 5)..randomLimit))
        return code
    }
}
