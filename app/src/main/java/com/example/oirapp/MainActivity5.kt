package com.example.oirapp

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.oirapp.databinding.ActivityMain5Binding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso


class MainActivity5 : AppCompatActivity() {
    private lateinit var storageReference: StorageReference
    private lateinit var binding: ActivityMain5Binding
    private var imagenUri: Uri? = null
    var firebaseStorage : FirebaseStorage? = null
            var firebaseDatabase : FirebaseDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain5Binding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.userProfileImage.setOnClickListener {
            seleccionarImg()
        }
        val editText: EditText = findViewById(R.id.editTextText)


        val imageView: ImageView = findViewById(R.id.user_profile_image)
        Glide.with(this).load(R.drawable.user_placeholder)
            .transform(CenterCrop(), RoundedCornersTransformation(32)).into(imageView)

        val userEmail = intent.getStringExtra("USER_EMAIL")
        val emailTextView: TextView = findViewById(R.id.textView2)
        emailTextView.text = userEmail

        val spinner: Spinner = findViewById(R.id.spinner)
        ArrayAdapter.createFromResource(
            this, R.array.roles_array, R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spinner.adapter = adapter
        }
        val continuarButton : Button = findViewById(R.id.button)
        continuarButton.setOnClickListener {
            val database = Firebase.database
            val myRef = database.getReference("Usuarios")
            val usuario = Usuario(editText.text.toString(), spinner.selectedItem.toString())

            myRef.setValue(usuario)
        }
        // Initialize Firebase Storage
        storageReference = FirebaseStorage.getInstance().reference
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("gs://oir-app-2e461.appspot.com/profile_images/1723171391573.jpg")

        // Obtén la URL de descarga
        storageRef.downloadUrl.addOnSuccessListener { uri: Uri? ->
            // Usa Picasso para cargar la imagen en el ImageView
            Picasso.get().load(uri).into(imageView)
        }.addOnFailureListener { exception: Exception? ->
            // Maneja cualquier error
            Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show()
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
            .compress( 1024)
        .maxResultSize(1080, 1080)
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
                    val fileReference = storageReference.child("profile_images/${System.currentTimeMillis()}.jpg")
                    fileReference.putFile(it)
                        .addOnSuccessListener { taskSnapshot ->
                            fileReference.downloadUrl.addOnSuccessListener { uri ->
                                // Handle the success, e.g., save the URL to the database
                                Toast.makeText(this, "Imagen subida exitosamente", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al subir la imagen: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            else {
                Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
            }
        }
}