package com.example.oirapp.data.network

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    @SerialName("id_mensaje") val id: Int,
    @SerialName("mensaje") val message: String,
    @SerialName("id_usuario") val userId: String,
    @SerialName("fecha_envio") val sentAt: Instant,
    @SerialName("id_grupo") val groupId: Int,
    @SerialName("sender_info") val senderInfo: SenderInfo,
)

@Serializable
data class SenderInfo(
    @SerialName("nombre") val name: String,
    @SerialName("rol") val role: String,
)
