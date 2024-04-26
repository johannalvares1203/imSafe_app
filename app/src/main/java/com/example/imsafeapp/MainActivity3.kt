package com.example.imsafeapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth


class MainActivity3 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            val intent = Intent(this, Homepage::class.java)
            startActivity(intent)
            finish()
        }


        val Registration: TextView = findViewById(R.id.register)
        val Login: TextView = findViewById(R.id.login)

        Registration.setOnClickListener {
            val intent = Intent(this, Part1_Registration::class.java)
            startActivity(intent)
            finish()
        }

        Login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}