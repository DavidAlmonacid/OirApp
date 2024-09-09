package com.example.oirapp

import android.os.Bundle
import android.util.Log
import android.util.Patterns.EMAIL_ADDRESS
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.oirapp.data.network.SupabaseClient.supabaseClient
import com.example.oirapp.databinding.ActivityCrearCuentaBinding
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class CrearCuentaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCrearCuentaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCrearCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.createAccountLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set the spinner options
        ArrayAdapter.createFromResource(
            this, R.array.roles_array, R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            binding.roleSpinner.adapter = adapter
        }

        binding.createAccountButton.setOnClickListener {
            val emailText = binding.emailEditTextCreateAccount.text.toString()
            val passwordText = binding.passwordEditTextCreateAccount.text.toString()
            val nameText = binding.nameEditTextCreateAccount.text.toString()
            val rol = binding.roleSpinner.selectedItem.toString()

            if (emailText.isEmpty() || passwordText.isEmpty() || nameText.isEmpty()) {
                Toast.makeText(
                    baseContext,
                    "Por favor, ingrese los campos requeridos.",
                    Toast.LENGTH_SHORT,
                ).show()

                return@setOnClickListener
            }

            if (passwordText.length < 6) {
                Toast.makeText(
                    baseContext,
                    "La contraseña debe tener al menos 6 caracteres.",
                    Toast.LENGTH_SHORT,
                ).show()

                return@setOnClickListener
            }

            if (!EMAIL_ADDRESS.matcher(emailText).matches()) {
                Toast.makeText(
                    baseContext,
                    "Por favor, ingrese un correo electrónico válido.",
                    Toast.LENGTH_SHORT,
                ).show()

                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    signUpNewUser(
                        userEmail = emailText,
                        userPassword = passwordText,
                        userName = nameText,
                        userRol = rol,
                    )

                    Toast.makeText(
                        baseContext,
                        "Se ha enviado un correo de verificación a $emailText",
                        Toast.LENGTH_SHORT,
                    ).show()
                } catch (e: Exception) {
                    Log.e("MainActivity3", "Error al crear la cuenta: ${e.message}")
                }
            }
        }
    }

    private suspend fun signUpNewUser(
        userEmail: String,
        userPassword: String,
        userName: String,
        userRol: String,
    ) {
        supabaseClient.auth.signUpWith(Email) {
            email = userEmail
            password = userPassword
            data = buildJsonObject {
                put("nombre", userName)
                put("rol", userRol)
                put("imagen_url", "")
            }
        }
    }
}