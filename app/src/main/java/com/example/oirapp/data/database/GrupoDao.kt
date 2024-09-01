package com.example.tuapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tuapp.data.entities.Grupo

@Dao
interface GrupoDao {

    @Insert
    suspend fun insertGrupo(grupo: Grupo)

    @Query("SELECT * FROM Grupo WHERE id_grupo = :id")
    suspend fun getGrupoById(id: Int): Grupo?

    @Query("SELECT * FROM Grupo")
    suspend fun getAllGrupos(): List<Grupo>

    @Query("DELETE FROM Grupo WHERE id_grupo = :id")
    suspend fun deleteGrupo(id: Int)
}
