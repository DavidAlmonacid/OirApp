package com.example.oirapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.oirapp.data.entities.Estudiante

@Dao
interface EstudianteDao {

    @Insert
    suspend fun insertEstudiante(estudiante: Estudiante)

    @Query("SELECT * FROM estudiantes WHERE id_estudiante = :id")
    suspend fun getEstudianteById(id: Int): Estudiante?

    @Query("SELECT * FROM estudiantes")
    suspend fun getAllEstudiantes(): List<Estudiante>

    @Query("DELETE FROM estudiantes WHERE id_estudiante = :id")
    suspend fun deleteEstudiante(id: Int)
}
