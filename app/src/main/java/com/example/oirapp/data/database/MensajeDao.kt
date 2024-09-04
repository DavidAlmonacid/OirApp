package com.example.oirapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.oirapp.data.entities.Mensaje

@Dao
interface MensajeDao {

    @Insert
    suspend fun insertMensaje(mensaje: Mensaje)

    @Query("SELECT * FROM mensajes WHERE id_grupo = :idGrupo ORDER BY fecha_envio ASC")
    suspend fun getMensajesByGrupo(idGrupo: Int): List<Mensaje>

    @Query("DELETE FROM mensajes WHERE id_mensaje = :id")
    suspend fun deleteMensaje(id: Int)
}
