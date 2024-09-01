package com.example.oirapp.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "docentes",
    foreignKeys = [ForeignKey(
        entity = Usuario::class,
        parentColumns = ["id_usuario"],
        childColumns = ["id_usuario"],
        onDelete = ForeignKey.CASCADE
    )],
)
data class Docente(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id_docente") val docenteId: Int = 0,
    @ColumnInfo(name = "id_usuario") val usuarioId: String,
    val nombre: String
)