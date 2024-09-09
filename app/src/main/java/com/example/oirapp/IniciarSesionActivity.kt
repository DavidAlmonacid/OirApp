package com.example.oirapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.oirapp.data.network.SupabaseClient.supabaseClient
import com.example.oirapp.databinding.ActivityIniciarSesionBinding
import com.example.oirapp.docente.GruposDocenteActivity
import com.example.oirapp.estudiante.GruposEstudianteActivity
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.launch
import kotlinx.serialization.json.jsonPrimitive

class IniciarSesionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIniciarSesionBinding

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

        binding.ingresarBoton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    baseContext,
                    "Por favor, ingrese los campos requeridos.",
                    Toast.LENGTH_SHORT,
                ).show()

                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    signInWithEmail(userEmail = email, userPassword = password)

                    val user = supabaseClient.auth.currentSessionOrNull()?.user
                    val userRole = user?.userMetadata?.get("rol")?.jsonPrimitive?.content

                    when (userRole) {
                        "Docente" -> {
                            val intent = Intent(
                                this@IniciarSesionActivity, GruposDocenteActivity::class.java
                            )
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }

                        "Estudiante" -> {
                            val intent = Intent(
                                this@IniciarSesionActivity, GruposEstudianteActivity::class.java
                            )
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }

                        else -> {
                            Toast.makeText(
                                baseContext,
                                "Error al iniciar sesión. Rol no válido.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        baseContext,
                        "Se produjo un error al iniciar sesión.",
                        Toast.LENGTH_SHORT,
                    ).show()

                    Log.e("IniciarSesionActivity", "Error al iniciar sesión: ${e.message}")
                }
            }
        }

        binding.registrarTextView.setOnClickListener {
            val intent = Intent(this, CrearCuentaActivity::class.java)
            startActivity(intent)
        }
    }

    private suspend fun signInWithEmail(userEmail: String, userPassword: String) {
        supabaseClient.auth.signInWith(Email) {
            email = userEmail
            password = userPassword
        }
    }
}