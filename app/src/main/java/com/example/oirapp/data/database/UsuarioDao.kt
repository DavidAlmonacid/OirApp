package com.example.oirapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.oirapp.data.entities.Usuario

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE id_usuario = :id")
    suspend fun getUser(id: String): Usuario

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(usuario: Usuario)

    // Update the user's information
    @Query("UPDATE usuarios SET rol = :rol, imagen_url = :imageUrl, correo = :email, contrasena = :contrasena WHERE id_usuario = :id")
    suspend fun updateUserInfo(id: String, contrasena: String, rol: String, email: String, imageUrl: String)

    @Query("DELETE FROM usuarios WHERE id_usuario = :id")
    suspend fun deleteUser(id: String)
}