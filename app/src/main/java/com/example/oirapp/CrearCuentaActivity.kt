package com.example.oirapp

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.oirapp.data.network.SupabaseClient.supabaseClient
import com.example.oirapp.databinding.ActivityCrearCuentaBinding
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

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
            val rol = binding.roleSpinner.selectedItem.toString().lowercase()

            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(
                    baseContext,
                    "Por favor, ingrese su correo electrónico y contraseña.",
                    Toast.LENGTH_SHORT,
                ).show()

                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    createAccount(emailText, passwordText, rol)
                } catch (e: Exception) {
                    Log.e("MainActivity3", "Error al crear la cuenta: ${e.message}")
                }
            }
        }
    }

    @Serializable
    data class UsuarioInsert(
        val correo: String,
        val contrasena: String,
        val rol: String,
    )

    // Create a new account with the supabase client
    private suspend fun createAccount(correo: String, contrasena: String, rol: String) {
        val user = UsuarioInsert(
            correo = correo,
            contrasena = contrasena,
            rol = rol,
        )

        try {
            supabaseClient.from("usuarios").insert(user)
            Toast.makeText(baseContext, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("MainActivity3", "Error al insertar usuario: ${e.message}")
        }
    }
}