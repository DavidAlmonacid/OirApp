package com.example.oirapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.oirapp.data.database.AppDatabase
import com.example.oirapp.data.entities.Usuario
import com.example.oirapp.databinding.ActivityMain3Binding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class MainActivity3 : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMain3Binding
    private lateinit var room: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        room = Room.databaseBuilder(this, AppDatabase::class.java, "database-name").build()

        lifecycleScope.launch {
            val user = room.usuarioDao().insert(
                Usuario(
                    usuarioId = "1",
                    contrasena = "123456",
                    rol = "Estudiante",
                    correo = "francisco@gmail.com",
                    imagenUrl = "https://www.google.com"
                )
            )
        }
        auth = Firebase.auth

        val email: EditText = findViewById(R.id.editTextTextEmailAddress)
        val password: EditText = findViewById(R.id.editTextTextPassword)
        val button: Button = findViewById(R.id.button)

        // Set the spinner options
        ArrayAdapter.createFromResource(
            this, R.array.roles_array, R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            binding.roleSpinner.adapter = adapter
        }
        button.setOnClickListener {
            createAccount(email.text.toString(), password.text.toString(), binding.roleSpinner.selectedItem.toString())
        }
    }

    private fun createAccount(email: String, password: String, rol : String) {
        lifecycleScope.launch {
            val user = Usuario(
                //usuarioId = "1", // You might want to generate a unique ID
                contrasena = password,
                rol = rol,
                correo = email,
                imagenUrl = "https://www.google.com" // Placeholder URL
            )
            room.usuarioDao().insert(user)
            Toast.makeText(
                baseContext,
                "Cuenta creada exitosamente.",
                Toast.LENGTH_SHORT,
            ).show()
            val intent = Intent(this@MainActivity3, IniciarSesionActivity::class.java)
            startActivity(intent)
        }
    }
}