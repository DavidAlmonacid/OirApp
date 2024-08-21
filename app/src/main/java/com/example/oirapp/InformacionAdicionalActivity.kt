package com.example.oirapp

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.oirapp.databinding.ActivityInformacionAdicionalBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class InformacionAdicionalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInformacionAdicionalBinding
    private var imagenUri: Uri? = null
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityInformacionAdicionalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.informacionAdicionalLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Storage
        storageReference = FirebaseStorage.getInstance().reference

        binding.changeProfileImageButton.setOnClickListener {
            seleccionarImg()
        }

        // Set the user's email
        val userEmail = intent.getStringExtra("USER_EMAIL")
        binding.emailTextView.text = userEmail

        // Set the spinner options
        ArrayAdapter.createFromResource(
            this, R.array.roles_array, R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            binding.roleSpinner.adapter = adapter
        }

        // Save the user's data to the Realtime Database
        binding.continueButton.setOnClickListener {
            val nombre = binding.nombreUsuarioEditText.text.toString()
            val rol = binding.roleSpinner.selectedItem.toString()

            if (nombre.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese su nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Upload image to Firebase Storage
            imagenUri?.let {
                val fileReference = storageReference.child(
                    "profile_images/${
                        nombre.lowercase().replace(" ", "-")
                    }.jpg"
                )

                fileReference.putFile(it).addOnSuccessListener {
                    fileReference.downloadUrl.addOnSuccessListener { downloadUri ->
                        // Save the download URL and user email to the database
                        saveUserDataToDatabase(
                            nombre = nombre,
                            rol = rol,
                            imageUrl = downloadUri.toString(),
                            email = userEmail!!
                        )
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(
                        this, "Error al subir la imagen: ${e.message}", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        /*binding.signOutButton.setOnClickListener {
            Firebase.auth.signOut()
            finish()

            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }*/

        // Registrar el callback para el botón de retroceso
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity() // Cierra todas las actividades y la aplicación
            }
        })
    }

    private fun seleccionarImg() {
        ImagePicker.with(this).crop().compress(720).maxResultSize(720, 720).createIntent { intent ->
            resultadoImg.launch(intent)
        }
    }

    private val resultadoImg =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == Activity.RESULT_OK) {
                val data = resultado.data
                imagenUri = data!!.data
                binding.userProfileImage.setImageURI(imagenUri)
            } else {
                Toast.makeText(
                    this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun saveUserDataToDatabase(
        nombre: String,
        rol: String,
        imageUrl: String,
        email: String,
    ) {
        val database = Firebase.database
        val myRef = database.getReference("Usuarios")
        val usuario = Usuario(
            nombre = nombre,
            rol = rol,
            imageUrl = imageUrl,
            email = email,
        )

        myRef.setValue(usuario).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Datos guardados exitosamente", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
