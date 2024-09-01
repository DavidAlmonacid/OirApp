package com.example.oirapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE id_usuario = :id")
    suspend fun getUser(id: String): Usuario

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(usuario: Usuario)

    // Update the user's information
    @Query("UPDATE usuarios SET rol = :rol, imageUrl = :imageUrl WHERE id_usuario = :id")
    suspend fun updateUserInfo(id: String, nombre: String, rol: String, email: String, imageUrl: String)

    @Query("DELETE FROM usuarios WHERE id_usuario = :id")
    suspend fun deleteUser(id: String)
}