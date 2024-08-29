package com.example.oirapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.oirapp.databinding.ActivityIniciarSesionBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class IniciarSesionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIniciarSesionBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityIniciarSesionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.iniciarSesionLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        binding.ingresarBoton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    baseContext,
                    "Por favor, ingrese su correo electrónico y contraseña.",
                    Toast.LENGTH_SHORT,
                ).show()

                return@setOnClickListener
            }

            signIn(email = email, password = password)
        }

        binding.registrarTextView.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser

                user?.let {
                    val userEmail = it.email

                    val intent = Intent(this, InformacionAdicionalActivity::class.java)
                    intent.apply { putExtra("USER_EMAIL", userEmail) }
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                    finish()
                }
            } else {
                Toast.makeText(
                    baseContext,
                    "Error al iniciar sesión.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }
}