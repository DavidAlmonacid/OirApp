package com.example.oirapp.docente

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.oirapp.R
import com.example.oirapp.databinding.ActivityGruposDocenteBinding

class GruposDocenteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGruposDocenteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityGruposDocenteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get the data from the previous activity
        val userImageUrl = intent.getStringExtra("USER_IMAGEN_URL")!!
        val userName = intent.getStringExtra("USER_NOMBRE")!!
        val userRole = intent.getStringExtra("USER_ROL")!!

        // Set the user's name and role
        binding.docenteNombreTv.text = userName
        binding.docenteRolTv.text = userRole

        // Load the user's image
//        Glide.with(this)
//            .load(userImageUrl)
//            .placeholder(R.drawable.user_placeholder)
//            .error(R.drawable.user_placeholder)
//            .into(binding.docenteImagenIv)
    }
}