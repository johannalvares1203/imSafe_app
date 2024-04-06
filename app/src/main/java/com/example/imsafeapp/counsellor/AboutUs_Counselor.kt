package com.example.imsafeapp.counsellor

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.imsafeapp.AboutUs
import com.example.imsafeapp.Homepage
import com.example.imsafeapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso

class AboutUs_Counselor : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us_counselor)

        val goback = findViewById<ImageView>(R.id.backbutton)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
            val itemId = item.itemId
            if (itemId == R.id.home) {
                // Lets the user go back to homepage
                startActivity(Intent(this@AboutUs_Counselor, Counsellor_Homepage::class.java))
                finish()
                return@setOnNavigationItemSelectedListener true
            } else if (itemId == R.id.aboutus) {
                startActivity(Intent(this@AboutUs_Counselor, AboutUs_Counselor::class.java))
                finish()
                return@setOnNavigationItemSelectedListener true
            } else if (itemId == R.id.tips) {
                // Handle Sub Option 1 click
                return@setOnNavigationItemSelectedListener true
            } else if (itemId == R.id.community) {
                // Handle Sub Option 1 click
                return@setOnNavigationItemSelectedListener true
            }
            false
        }

        // Set click listener for going back to the homepage
        goback.setOnClickListener {
            startActivity(Intent(this@AboutUs_Counselor, Counsellor_Homepage::class.java))
            finish()
        }
        val account = GoogleSignIn.getLastSignedInAccount(this)
        // Replaces image with one associated with Google Account
        val profileImageView = findViewById<ImageView>(R.id.profileIcon)
        if (account != null) {
            // Load the profile photo using Picasso library (or any other image loading library)
            Picasso.get().load(account.photoUrl).into(profileImageView)
        } else {
            // If the user is not signed in with Google, you can set a default image or hide the ImageView
            profileImageView.setImageResource(R.drawable.baseline_add_photo_alternate_24)
        }
    }
}