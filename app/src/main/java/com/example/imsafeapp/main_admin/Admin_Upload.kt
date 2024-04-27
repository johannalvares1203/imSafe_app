package com.example.imsafeapp.main_admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.imsafeapp.R
import com.example.imsafeapp.databinding.ActivityAdminAddBinding
import com.example.imsafeapp.databinding.ActivityAdminUploadBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class Admin_Upload : AppCompatActivity() {

    private lateinit var binding: ActivityAdminUploadBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUploadBinding.inflate(layoutInflater)
        setContentView(binding.root) // Move this line here

        // Now you can access your views using binding directly
        val constituencySpinner = binding.constituencySpinner

        // Sample list of constituencies
        val constituencies = listOf(
            "Aldona", "Benaulim", "Bicholim", "Calangute", "Canacona", "Cortalim", "Cuncolim",
            "Cumbarjua", "Curchorem", "Curtorim", "Dabolim", "Fatorda", "Margao", "Maem", "Mandrem",
            "Marcaim", "Mapusa", "Mormugao", "Navelim", "Nuvem", "Panaji", "Pernem (SC)", "Ponda",
            "Poriem", "Porvorim", "Priol", "Quepem", "Saligao", "Sanquelim", "Sanvordem", "Santa Cruz",
            "Sanguem", "Siroda", "Siolim", "St. Andre", "Taleigao", "Tivim", "Valpoi", "Vasco Da Gama", "Velim"
        )

        // Create an ArrayAdapter using the list of constituencies and a default spinner layout
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, constituencies)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        constituencySpinner.adapter = adapter

        val daySpinner: Spinner = findViewById(R.id.daySpinner)
        val loginLink = findViewById<TextView>(R.id.loginLink)
        val monthSpinner: Spinner = findViewById(R.id.monthSpinner)
        val yearSpinner: Spinner = findViewById(R.id.yearSpinner)
        val ageEditText: EditText = findViewById(R.id.editTextAge)

        // Populate day spinner
        val days = (1..31).toList()
        val dayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, days)
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        daySpinner.adapter = dayAdapter

        // Populate month spinner
        val months = arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        val monthAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        monthSpinner.adapter = monthAdapter

        // Populate year spinner
        val years = (1900..2024).toList()
        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSpinner.adapter = yearAdapter

        // Set listener for spinners
        daySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                updateAge(ageEditText, daySpinner, monthSpinner, yearSpinner)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                updateAge(ageEditText, daySpinner, monthSpinner, yearSpinner)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                updateAge(ageEditText, daySpinner, monthSpinner, yearSpinner)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        setContentView(binding.root)
        binding.addUserButton.setOnClickListener {
            val firstname = binding.editTextFirstName.text.toString()
            val lastname = binding.editTextLastName.text.toString()
            val age = binding.editTextAge.text.toString()
            val gender = binding.editTextGender.text.toString()
            val constituency = binding.constituencySpinner.selectedItem.toString()
            val address = binding.editTextAddresss.text.toString()
            val email = binding.Email.text.toString()
            val password = binding.Password.text.toString()

            database = FirebaseDatabase.getInstance().getReference("Users")
            val users = User(firstname, lastname, age, gender, constituency, address, email, password)

            database.child(email).setValue(users).addOnSuccessListener {
                binding.editTextFirstName.text?.clear()
                binding.editTextLastName.text?.clear()
                binding.editTextAge.text?.clear()
                binding.editTextGender.text?.clear()
                binding.constituencySpinner.setSelection(0)
                binding.editTextAddresss.text.clear()
                binding.Email.text?.clear()
                binding.Password.text?.clear()
                Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@Admin_Upload, Admin_Add::class.java)
                startActivity(intent)
            }.addOnFailureListener{
                Toast.makeText(this,"Failed to add users!",Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun updateAge(ageEditText: EditText, daySpinner: Spinner, monthSpinner: Spinner, yearSpinner: Spinner) {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR) - yearSpinner.selectedItem.toString().toInt()
        val month = monthSpinner.selectedItemPosition + 1
        val day = daySpinner.selectedItem.toString().toInt()

        cal.set(yearSpinner.selectedItem.toString().toInt(), month - 1, day)

        val dob = cal.timeInMillis
        val now = System.currentTimeMillis()
        val age = ((now - dob) / (1000L * 60 * 60 * 24 * 365.25)).toInt()

        ageEditText.setText(age.toString())
    }
}