package com.example.oirapp.data.network

import com.example.oirapp.data.network.SupabaseClient.supabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class Group(
    @SerialName("id_grupo")
    val id: Int,
    @SerialName("nombre_grupo")
    val name: String,
    @SerialName("codigo_acceso")
    val code: String,
    @SerialName("id_docente")
    val idDocente: String,)


class GroupApi {
    suspend fun createGroup() {
        val table = supabaseClient.postgrest["grupos"]
        table.insert(buildJsonObject {
            put("nombre_grupo", "Grupo 1")
            put("codigo_acceso", "G1")
            put("id_docente", "9499763e-b013-4e76-a714-52d8ba052886")
        })

    }

}