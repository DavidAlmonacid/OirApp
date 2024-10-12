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
import io.github.jan.supabase.postgrest.rpc
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

    private val _studentGroups = MutableStateFlow<List<Group>>(emptyList())
    val studentGroups: StateFlow<List<Group>> = _studentGroups

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

    fun getCreatedGroups(idDocente: String) {
        viewModelScope.launch {
            try {
                val fetchedGroups = withContext(Dispatchers.IO) {
                    supabaseClient.from("grupos").select {
                        order(column = "id_grupo", order = Order.ASCENDING)
                        filter { eq("id_docente", idDocente) }
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
            return
        }

        if (groupName in getTeacherGroupNames()) {
            errorMessage = "El nombre del grupo ya existe."
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

                getCreatedGroups(idDocente)
                resetData()
            } catch (e: Exception) {
                println("GruposViewModel.createGroup: Error: ${e.message}")
            }
        }
    }

    fun updateGroupName(groupId: Int, newName: String, idDocente: String) {
        viewModelScope.launch {
            try {
                supabaseClient.from("grupos").update({
                    set("nombre_grupo", newName)
                }) {
                    filter {
                        eq("id_grupo", groupId)
                    }
                }

                this@GruposViewModel.setShowDialog(false)

                getCreatedGroups(idDocente)
                resetData()
            } catch (e: Exception) {
                println("GruposViewModel.editGroup: Error: ${e.message}")
            }
        }
    }

    fun deleteGroup(groupId: Int, idDocente: String) {
        viewModelScope.launch {
            try {
                supabaseClient.from("grupos").delete {
                    filter {
                        eq("id_grupo", groupId)
                    }
                }
                getCreatedGroups(idDocente)
            } catch (e: Exception) {
                println("GruposViewModel.deleteGroup: Error: ${e.message}")
            }
        }
    }

    private fun getStudentGroupCodes(): List<String> {
        return _studentGroups.value.map { it.code }
    }

    fun getJoinedGroups(idEstudiante: String) {
        viewModelScope.launch {
            try {
                val fetchedGroups = withContext(Dispatchers.IO) {
                    supabaseClient.postgrest.rpc("get_joined_groups", buildJsonObject {
                        put("p_id_estudiante", idEstudiante)
                    }).decodeList<Group>()
                }

                _studentGroups.value = fetchedGroups
            } catch (e: Exception) {
                println("GruposViewModel.getJoinedGroups: Error: ${e.message}")
            }
        }
    }

    fun joinGroup(accessCode: String, idEstudiante: String) {
        if (accessCode.isEmpty()) {
            errorMessage = "Ingrese el código de acceso."
            return
        }

        if (accessCode in getStudentGroupCodes()) {
            errorMessage = "Ya estás en este grupo."
            return
        }

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
                    errorMessage = "Código de acceso inválido."
                    return@launch
                }

                val table = supabaseClient.postgrest["estudiantes_grupos"]

                withContext(Dispatchers.IO) {
                    table.insert(buildJsonObject {
                        put("id_estudiante", idEstudiante)
                        put("id_grupo", group.id)
                    })
                }

                this@GruposViewModel.setShowDialog(false)

                getJoinedGroups(idEstudiante)
                resetData()
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
