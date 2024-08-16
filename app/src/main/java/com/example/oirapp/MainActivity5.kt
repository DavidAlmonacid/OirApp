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
import com.example.oirapp.databinding.ActivityMain5Binding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class MainActivity5 : AppCompatActivity() {
    private lateinit var binding: ActivityMain5Binding
    private var imagenUri: Uri? = null
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMain5Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.changeProfileImageButton.setOnClickListener {
            seleccionarImg()
        }

        // Initialize Firebase Storage
        storageReference = FirebaseStorage.getInstance().reference
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("profile_images/1723171391573.jpg")

        // Obtén la URL de descarga
        storageRef.downloadUrl.addOnSuccessListener { uri: Uri? ->
            Picasso.get().load(uri).into(binding.userProfileImage)
        }.addOnFailureListener { exception: Exception? ->
            Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show()
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
            val database = Firebase.database
            val myRef = database.getReference("Usuarios")
            val usuario = Usuario(
                binding.nombreUsuarioEditText.text.toString(),
                binding.roleSpinner.selectedItem.toString()
            )

            myRef.setValue(usuario)
        }

        /*val signOutButton: Button = findViewById(R.id.button2)
        signOutButton.setOnClickListener {
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
        ImagePicker.with(this)
            .crop()
            .compress(720)
            .maxResultSize(720, 720)
            .createIntent { intent ->
                resultadoImg.launch(intent)
            }
    }

    private val resultadoImg =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == Activity.RESULT_OK) {
                val data = resultado.data
                imagenUri = data!!.data
                binding.userProfileImage.setImageURI(imagenUri)

                // Upload image to Firebase Storage
                imagenUri?.let {
                    val fileReference =
                        storageReference.child("profile_images/${System.currentTimeMillis()}.jpg")

                    fileReference.putFile(it)
//                        .addOnSuccessListener { taskSnapshot ->
//                            fileReference.downloadUrl.addOnSuccessListener { uri ->
//                                // Handle the success, e.g., save the URL to the database
//                                Toast.makeText(
//                                    this,
//                                    "Imagen subida exitosamente",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                this,
                                "Error al subir la imagen: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            } else {
                Toast.makeText(
                    this,
                    "No se seleccionó ninguna imagen",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
}
