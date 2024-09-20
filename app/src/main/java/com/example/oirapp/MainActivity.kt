package com.example.oirapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.oirapp.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        println("onStart: Starting App")

        /*
         * TODO: Verificar si el usuario ya ha iniciado sesión,
         *  si es así, redirigir a la pantalla de Grupos dependiendo del rol del usuario,
         *  si es 'Estudiante' redirigir a la pantalla de Grupos Estudiante,
         *  si es 'Docente' redirigir a la pantalla de Grupos Docente.
         */

//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            val intent = Intent(this, InformacionAdicionalActivity::class.java)
//            intent.apply { putExtra("USER_EMAIL", currentUser.email) }
//            startActivity(intent)
//            finish()
//        }
    }
}
