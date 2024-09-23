package com.example.oirapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.oirapp.data.network.SupabaseClient.supabaseClient
import com.example.oirapp.ui.theme.MyApplicationTheme
import io.github.jan.supabase.gotrue.auth

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

        /*
         * TODO: Verificar si el usuario ya ha iniciado sesión,
         *  si es así, redirigir a la pantalla de Grupos.
         */

        val session = supabaseClient.auth.currentSessionOrNull()
        println("MainActivity.onStart: Session: $session")

        //if (session != null) {
            // code
        //}
    }
}
