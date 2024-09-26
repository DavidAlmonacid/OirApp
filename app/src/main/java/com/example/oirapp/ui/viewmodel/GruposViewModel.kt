package com.example.oirapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.oirapp.data.network.Group
import com.example.oirapp.data.network.SupabaseClient.supabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class GruposViewModel : BaseViewModel() {
    private val _teacherGroups = MutableStateFlow<List<Group>>(emptyList())
    val teacherGroups: StateFlow<List<Group>> = _teacherGroups

    var userInput by mutableStateOf("")
        private set

    var errorMessage by mutableStateOf("")
        private set

    fun updateUserInput(input: String) {
        userInput = input
    }

    fun resetData() {
        userInput = ""
        errorMessage = ""
    }

    private fun getTeacherGroupNames(): List<String> {
        return teacherGroups.value.map { it.name }
    }

    fun getCreatedGroups() {
        viewModelScope.launch {
            try {
                val fetchedGroups = withContext(Dispatchers.IO) {
                    supabaseClient.from("grupos").select().decodeList<Group>()
                }

                _teacherGroups.value = fetchedGroups
            } catch (e: Exception) {
                println("GruposViewModel.getCreatedGroups: Error: ${e.message}")
            }
        }
    }

    fun createGroup(groupName: String, idDocente: String) {
        if (groupName.isEmpty()) {
            errorMessage = "Ingrese el nombre del grupo."
            this.setShowDialog(true)
            return
        }

        if (groupName in getTeacherGroupNames()) {
            errorMessage = "El nombre del grupo ya existe."
            this.setShowDialog(true)
            return
        }

        viewModelScope.launch {
            try {
                val table = supabaseClient.postgrest["grupos"]

                withContext(Dispatchers.IO) {
                    table.insert(buildJsonObject {
                        put("nombre_grupo", groupName)
                        put("codigo_acceso", generateAccessCode())
                        put("id_docente", idDocente)
                    })
                }

                this@GruposViewModel.setShowDialog(false)

                getCreatedGroups()
                resetData()
            } catch (e: Exception) {
                println("GruposViewModel.createGroup: Error: ${e.message}")
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
