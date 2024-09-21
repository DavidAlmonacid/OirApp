package com.example.oirapp.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.oirapp.data.network.SupabaseClient.supabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class GruposViewModel : BaseViewModel() {
    fun createGroup(groupName: String) {
        val accessCode = generateAccessCode()
        viewModelScope.launch {
            createGroupSuspend(groupName, accessCode)
        }
    }

    private suspend fun createGroupSuspend(groupName: String, accessCode: String) {
        val table = supabaseClient.postgrest["grupos"]
        table.insert(buildJsonObject {
            put("nombre_grupo", groupName)
            put("codigo_acceso", accessCode)
            put("id_docente", "9499763e-b013-4e76-a714-52d8ba052886")
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
