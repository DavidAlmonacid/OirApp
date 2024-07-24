package com.example.oirapp

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop

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
    }
}