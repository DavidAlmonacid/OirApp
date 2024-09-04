package com.example.oirapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.oirapp.data.entities.Docente
import com.example.oirapp.data.entities.Estudiante
import com.example.oirapp.data.entities.EstudiantesGrupos
import com.example.oirapp.data.entities.Grupo
import com.example.oirapp.data.entities.Mensaje
import com.example.oirapp.data.entities.Usuario

@Database(
    entities = [
        Usuario::class,
        Estudiante::class,
        Docente::class,
        Grupo::class,
        EstudiantesGrupos::class,
        Mensaje::class,
    ],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun estudianteDao(): EstudianteDao
    abstract fun docenteDao(): DocenteDao
    abstract fun grupoDao(): GrupoDao
    abstract fun estudiantesGruposDao(): EstudiantesGruposDao
    abstract fun mensajeDao(): MensajeDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}