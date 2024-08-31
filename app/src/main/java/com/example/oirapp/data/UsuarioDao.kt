package com.example.oirapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE id = :id")
    fun getUserById(id: String): Usuario

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(usuario: Usuario)

    @Query("DELETE FROM usuarios")
    suspend fun deleteAll()
}