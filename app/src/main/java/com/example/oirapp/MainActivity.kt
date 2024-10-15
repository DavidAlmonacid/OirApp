package com.example.oirapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.oirapp.record.AudioRecorderImpl
import com.example.oirapp.ui.theme.MyApplicationTheme
import java.io.File

class MainActivity : ComponentActivity() {
    private val recorder by lazy { AudioRecorderImpl(applicationContext) }
    private var audioFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp()
            }
        }
    }
}
