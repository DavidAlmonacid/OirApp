package com.example.oirapp.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "grupos",
    foreignKeys = [ForeignKey(
        entity = Docente::class,
        parentColumns = ["id_docente"],
        childColumns = ["id_docente"],
        onDelete = ForeignKey.CASCADE
    )],
)
data class Grupo(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id_grupo") val grupoId: Int = 0,
    @ColumnInfo(name = "nombre_grupo") val nombreGrupo: String,
    @ColumnInfo(name = "codigo_acceso") val codigoAcceso: String,
    @ColumnInfo(name = "id_docente") val docenteId: Int
)