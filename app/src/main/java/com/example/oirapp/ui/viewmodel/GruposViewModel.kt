package com.example.oirapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oirapp.data.network.Group
import com.example.oirapp.data.network.SupabaseClient.supabaseClient
import com.example.oirapp.ui.state.GroupState
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class GruposViewModel : BaseViewModel() {
    private val _groupState = MutableLiveData<GroupState>()
    val groupState: LiveData<GroupState> = _groupState

    private val _teacherGroups = MutableStateFlow<List<Group>>(emptyList())
    val teacherGroups: StateFlow<List<Group>> = _teacherGroups

    var userInput by mutableStateOf("")
        private set

    var errorMessage by mutableStateOf("")
        private set

    var groupId by mutableIntStateOf(0)
        private set

    fun updateUserInput(input: String) {
        userInput = input
    }

    fun updateGroupId(id: Int) {
        groupId = id
    }

    fun resetData() {
        userInput = ""
        errorMessage = ""
        groupId = 0
    }

    fun openDialog(state: GroupState) {
        this.setShowDialog(true)
        _groupState.value = state
    }

    private fun getTeacherGroupNames(): List<String> {
        return _teacherGroups.value.map { it.name }
    }

    fun getCreatedGroups() {
        viewModelScope.launch {
            try {
                val fetchedGroups = withContext(Dispatchers.IO) {
                    supabaseClient.from("grupos").select {
                        order(column = "id_grupo", order = Order.ASCENDING)
                    }.decodeList<Group>()
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

    fun updateGroupName(id: Int, newName: String) {
        viewModelScope.launch {
            try {
                supabaseClient.from("grupos").update({
                    set("nombre_grupo", newName)
                }) {
                    filter {
                        eq("id_grupo", id)
                    }
                }

                this@GruposViewModel.setShowDialog(false)

                getCreatedGroups()
                resetData()
            } catch (e: Exception) {
                println("GruposViewModel.editGroup: Error: ${e.message}")
            }
        }
    }

    fun deleteGroup(id: Int) {
        viewModelScope.launch {
            try {
                supabaseClient.from("grupos").delete {
                    filter {
                        eq("id_grupo", id)
                    }
                }
                getCreatedGroups()
            } catch (e: Exception) {
                println("GruposViewModel.deleteGroup: Error: ${e.message}")
            }
        }
    }

    fun joinGroup(accessCode: String, idEstudiante: String) {
        viewModelScope.launch {
            try {
                val group = withContext(Dispatchers.IO) {
                    supabaseClient.from("grupos").select {
                        filter {
                            eq("codigo_acceso", accessCode)
                        }
                    }.decodeSingleOrNull<Group>()
                }

                if (group == null) {
                    errorMessage = "Código de acceso incorrecto."
                    return@launch
                }

                val table = supabaseClient.postgrest["estudiantes_grupos"]

                withContext(Dispatchers.IO) {
                    table.insert(buildJsonObject {
                        put("id_estudiante", idEstudiante)
                        put("id_grupo", group.id)
                    })
                }
            } catch (e: Exception) {
                println("GruposViewModel.joinGroup: Error: ${e.message}")
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
