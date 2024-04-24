package com.example.imsafeapp.main_admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.imsafeapp.R
import com.example.imsafeapp.databinding.ActivityAdminDeleteBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Admin_Delete : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDeleteBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.deleteButton.setOnClickListener {
            val email = binding.deleteaccount.text.toString()
            if (email.isNotEmpty())
                deleteData(email)
            else
                Toast.makeText(this, "Please enter the phone number", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteData(email: String){
        val encodedEmail = email.replace(".", ",").replace("@", "")
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(encodedEmail).removeValue().addOnSuccessListener {
            binding.deleteaccount.text.clear()
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Unable to delete", Toast.LENGTH_SHORT).show()
        }
    }
}