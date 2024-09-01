package com.example.oirapp.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "estudiantes_grupos",
    foreignKeys = [
        ForeignKey(
            entity = Estudiante::class,
            parentColumns = ["id_estudiante"],
            childColumns = ["id_estudiante"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Grupo::class,
            parentColumns = ["id_grupo"],
            childColumns = ["id_grupo"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
)
data class EstudiantesGrupos(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "id_estudiante") val estudianteId: Int,
    @ColumnInfo(name = "id_grupo") val grupoId: Int
)