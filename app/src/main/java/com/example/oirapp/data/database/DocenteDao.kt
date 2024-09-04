package com.example.oirapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.oirapp.data.entities.Docente

@Dao
interface DocenteDao {

    @Insert
    suspend fun insertDocente(docente: Docente)

    @Query("SELECT * FROM docentes WHERE id_docente = :id")
    suspend fun getDocenteById(id: Int): Docente?

    @Query("SELECT * FROM docentes")
    suspend fun getAllDocentes(): List<Docente>

    @Query("DELETE FROM docentes WHERE id_docente = :id")
    suspend fun deleteDocente(id: Int)
}
