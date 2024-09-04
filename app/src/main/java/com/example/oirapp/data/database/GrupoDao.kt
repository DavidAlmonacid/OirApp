package com.example.oirapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.oirapp.data.entities.Grupo

@Dao
interface GrupoDao {

    @Insert
    suspend fun insertGrupo(grupo: Grupo)

    @Query("SELECT * FROM grupos WHERE id_grupo = :id")
    suspend fun getGrupoById(id: Int): Grupo?

    @Query("SELECT * FROM grupos")
    suspend fun getAllGrupos(): List<Grupo>

    @Query("DELETE FROM grupos WHERE id_grupo = :id")
    suspend fun deleteGrupo(id: Int)
}
