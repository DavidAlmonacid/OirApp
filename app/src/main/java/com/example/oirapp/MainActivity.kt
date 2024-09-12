package com.example.oirapp

import android.content.Intent
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

//    override fun onStart() {
//        super.onStart()
//
//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            val intent = Intent(this, InformacionAdicionalActivity::class.java)
//            intent.apply { putExtra("USER_EMAIL", currentUser.email) }
//            startActivity(intent)
//            finish()
//        }
//    }
}
