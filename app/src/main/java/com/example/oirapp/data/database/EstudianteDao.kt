package com.example.tuapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tuapp.data.entities.Estudiante

@Dao
interface EstudianteDao {

    @Insert
    suspend fun insertEstudiante(estudiante: Estudiante)

    @Query("SELECT * FROM Estudiante WHERE id_estudiante = :id")
    suspend fun getEstudianteById(id: Int): Estudiante?

    @Query("SELECT * FROM Estudiante")
    suspend fun getAllEstudiantes(): List<Estudiante>

    @Query("DELETE FROM Estudiante WHERE id_estudiante = :id")
    suspend fun deleteEstudiante(id: Int)
}
