package com.example.oirapp.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "mensajes",
    foreignKeys = [
        ForeignKey(
            entity = Grupo::class,
            parentColumns = ["id_grupo"],
            childColumns = ["id_grupo"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id_usuario"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
)
data class Mensaje(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id_mensaje") val mensajeId: Int = 0,
    @ColumnInfo(name = "id_grupo") val grupoId: Int,
    @ColumnInfo(name = "id_usuario") val usuarioId: Int,
    val mensaje: String,
    @ColumnInfo(name = "fecha_envio") val fechaEnvio: String
)