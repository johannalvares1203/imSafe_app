package com.example.imsafeapp.main_admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.imsafeapp.R
import com.example.imsafeapp.databinding.ActivityMainBinding
import com.example.imsafeapp.databinding.ActivityUsersBinding
import com.example.imsafeapp.databinding.ActivityViewDetailsBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

@Suppress("NAME_SHADOWING")
class ViewDetails : AppCompatActivity() {

    private lateinit var binding: ActivityViewDetailsBinding
    private lateinit var database : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.SearchButton.setOnClickListener {
            val searchPhone : String = binding.searchPhone.text.toString()
            if  (searchPhone.isNotEmpty()){
                readData(searchPhone)
            }else{
                Toast.makeText(this,"PLease enter the email address",Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun readData(email: String) {
        // Encode the email address to remove characters not allowed in Firebase paths
        val encodedEmail = email.replace(".", ",")

        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(encodedEmail).get().addOnSuccessListener {
            if (it.exists()){
                val firstname = it.child("name").value
                val lastname = it.child("name").value
                val age = it.child("name").value
                val gender = it.child("name").value
                val constituency = it.child("name").value
                val address = it.child("operator").value
                val email = it.child("location").value
                val password = it.child("location").value
                Toast.makeText(this,"Results Found",Toast.LENGTH_SHORT).show()
                binding.searchPhone.text.clear()
                binding.readFirstName.text = firstname.toString()
                binding.readLastName.text = lastname.toString()
                binding.readAge.text = age.toString()
                binding.readGender.text = gender.toString()
                binding.readConstituency.text = constituency.toString()
                binding.readAddress.text = address.toString()
                binding.readEmail.text = email.toString()
                binding.readPassword.text = password.toString()
            } else {
                Toast.makeText(this,"Email id does not exist",Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener{
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
        }
    }

}