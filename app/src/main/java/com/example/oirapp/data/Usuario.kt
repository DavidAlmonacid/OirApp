package com.example.oirapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey val id_usuario: String,
    val correo: String,
    val contrasena: String,
    val rol: String,
    val imageUrl: String?,
)