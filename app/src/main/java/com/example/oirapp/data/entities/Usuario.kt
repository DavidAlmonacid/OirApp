package com.example.oirapp.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey @ColumnInfo(name = "id_usuario") val usuarioId: String = UUID.randomUUID()
        .toString(),
    val correo: String,
    val contrasena: String,
    val rol: String,
    @ColumnInfo(name = "imagen_url") val imagenUrl: String?,
)