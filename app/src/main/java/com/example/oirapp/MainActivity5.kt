package com.example.oirapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity5 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main5)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // TODO: Get the user email from Firebase Auth and display it in the TextView
        // TODO: Implement the functionality to sign in the user automatically if they are already signed in
        // TODO: Implement the functionality to sign out the user

        val imageView: ImageView = findViewById(R.id.user_profile_image)
        Glide.with(this)
            .load(R.drawable.user_placeholder)
            .transform(CenterCrop(), RoundedCornersTransformation(32))
            .into(imageView)
        // Recuperar el correo electrónico del Intent
        val userEmail = intent.getStringExtra("USER_EMAIL")

        // Hacer algo con el correo electrónico, por ejemplo, mostrarlo en un TextView
        val emailTextView: TextView = findViewById(R.id.textView11)
        emailTextView.text = userEmail
        val signOutButton: LinearLayout = findViewById(R.id.Cerar_sesion)
        signOutButton.setOnClickListener {
            Firebase.auth.signOut()
            finish()
        }
    }
}