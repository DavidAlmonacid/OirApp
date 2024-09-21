package com.example.oirapp.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Group(
    @SerialName("id_grupo") val id: Int,
    @SerialName("nombre_grupo") val name: String,
    @SerialName("codigo_acceso") val code: String,
    @SerialName("id_docente") val idDocente: String,
)
