package com.example.tuapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tuapp.data.entities.Docente

@Dao
interface DocenteDao {

    @Insert
    suspend fun insertDocente(docente: Docente)

    @Query("SELECT * FROM Docente WHERE id_docente = :id")
    suspend fun getDocenteById(id: Int): Docente?

    @Query("SELECT * FROM Docente")
    suspend fun getAllDocentes(): List<Docente>

    @Query("DELETE FROM Docente WHERE id_docente = :id")
    suspend fun deleteDocente(id: Int)
}
