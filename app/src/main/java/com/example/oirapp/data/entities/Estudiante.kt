package com.example.oirapp.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "estudiantes",
    foreignKeys = [ForeignKey(
        entity = Usuario::class,
        parentColumns = ["id_usuario"],
        childColumns = ["id_usuario"],
        onDelete = ForeignKey.CASCADE
    )],
)
data class Estudiante(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id_estudiante") val estudianteId: Int = 0,
    @ColumnInfo(name = "id_usuario") val usuarioId: String,
    val nombre: String
)