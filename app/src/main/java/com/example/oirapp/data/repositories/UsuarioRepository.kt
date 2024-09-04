package com.example.oirapp.data.repositories

import androidx.annotation.WorkerThread
import com.example.oirapp.data.database.UsuarioDao
import com.example.oirapp.data.entities.Usuario

class UsuarioRepository(private val usuarioDao: UsuarioDao) {


    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(usuario : Usuario) {
        usuarioDao.insert(usuario)
    }
}