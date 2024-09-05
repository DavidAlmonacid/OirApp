package com.example.oirapp

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.oirapp.databinding.ActivityMain3Binding
import com.google.firebase.auth.FirebaseAuth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.util.UUID

class MainActivity3 : AppCompatActivity() {
    //private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMain3Binding
    //private lateinit var room: AppDatabase

    val supabase = createSupabaseClient(
        supabaseUrl = "https://lydqdbpjgddgkzcdooqe.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imx5ZHFkYnBqZ2RkZ2t6Y2Rvb3FlIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjU1NTE3NDIsImV4cCI6MjA0MTEyNzc0Mn0.SrJ_Ln3HB6o_EXdKp26raJrdhh8W6WdDuW6UZHJ8PUg",
    ) {
        install(Postgrest)
    }

    @Serializable
    data class Usuario(
        val usuarioId: String = UUID.randomUUID().toString(),
        val contrasena: String,
        val rol: String = Rol.ESTUDIANTE.name.lowercase(),
        val correo: String,
        val imagenUrl: String? = null,
    )

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

//        room = Room.databaseBuilder(this, AppDatabase::class.java, "database-name").build()
//
//        lifecycleScope.launch {
//            val user = room.usuarioDao().insert(
//                Usuario(
//                    usuarioId = "1",
//                    contrasena = "123456",
//                    rol = "Estudiante",
//                    correo = "francisco@gmail.com",
//                    imagenUrl = "https://www.google.com"
//                )
//            )
//        }
//        auth = Firebase.auth

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
            val emailText = email.text.toString()
            val passwordText = password.text.toString()
            val rol = binding.roleSpinner.selectedItem.toString()

            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Log.v("MainActivity3", "Por favor, ingrese su correo electrónico y contraseña.")
                return@setOnClickListener
            }

            lifecycleScope.launch {
                createAccount(emailText, passwordText, rol)
            }
        }
    }

    // Create a new account with the supabase client
    private suspend fun createAccount(correo: String, contrasena: String, rol: String) {
        val user = Usuario(
            correo = correo,
            contrasena = contrasena,
            rol = rol,
        )

        supabase.from("usuarios").insert(user) {
            Log.v("MainActivity3", "User created successfully.")
        }
    }

    //private fun createAccount(email: String, password: String, rol: String) {
//        lifecycleScope.launch {
//            val user = Usuario(
//                //usuarioId = "1", // You might want to generate a unique ID
//                contrasena = password,
//                rol = rol,
//                correo = email,
//                imagenUrl = "https://www.google.com" // Placeholder URL
//            )
//            //room.usuarioDao().insert(user)
//            Toast.makeText(
//                baseContext,
//                "Cuenta creada exitosamente.",
//                Toast.LENGTH_SHORT,
//            ).show()
//            val intent = Intent(this@MainActivity3, IniciarSesionActivity::class.java)
//            startActivity(intent)
//        }
    //}
}

enum class Rol {
    ESTUDIANTE,
    DOCENTE,
}