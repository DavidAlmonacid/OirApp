package com.example.tuapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tuapp.data.entities.EstudiantesGrupos

@Dao
interface EstudiantesGruposDao {

    @Insert
    suspend fun insertEstudianteGrupo(estudiantesGrupos: EstudiantesGrupos)

    @Query("SELECT * FROM EstudiantesGrupos WHERE id_estudiante = :idEstudiante")
    suspend fun getGruposByEstudianteId(idEstudiante: Int): List<EstudiantesGrupos>

    @Query("SELECT * FROM EstudiantesGrupos WHERE id_grupo = :idGrupo")
    suspend fun getEstudiantesByGrupoId(idGrupo: Int): List<EstudiantesGrupos>

    @Query("DELETE FROM EstudiantesGrupos WHERE id = :id")
    suspend fun deleteEstudianteGrupo(id: Int)
}
