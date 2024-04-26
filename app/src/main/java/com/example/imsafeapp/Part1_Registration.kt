package com.example.imsafeapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class Part1_Registration : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore: FirebaseFirestore


    private lateinit var firstname: String
    private lateinit var lastname: String
    private lateinit var age: String
    private lateinit var gender: String
    private lateinit var constituency: String
    private lateinit var address: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_part1_registration)

        firestore = FirebaseFirestore.getInstance()
        val constituencySpinner: Spinner = findViewById(R.id.constituencySpinner)

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

        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        val daySpinner: Spinner = findViewById(R.id.daySpinner)
        val loginLink = findViewById<TextView>(R.id.loginLink)
        val monthSpinner: Spinner = findViewById(R.id.monthSpinner)
        val yearSpinner: Spinner = findViewById(R.id.yearSpinner)
        val ageEditText: EditText = findViewById(R.id.editTextAge)
        auth = FirebaseAuth.getInstance()


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

        val continue_1 = findViewById<TextView>(R.id.buttonnext)


        continue_1.setOnClickListener {
            saveDataToFirestore()


            val firstname = findViewById<EditText>(R.id.editTextFirstName).text.toString()
            val lastname = findViewById<EditText>(R.id.editTextLastName).text.toString()
            val gender = findViewById<EditText>(R.id.editTextGender).text.toString()
            val age = findViewById<EditText>(R.id.editTextAge).text.toString()
            // birth date?????
            val selectedConstituency = findViewById<Spinner>(R.id.constituencySpinner).selectedItem.toString()
            val address = findViewById<EditText>(R.id.editTextAddresss).text.toString()

            val intent = Intent(this, Part2_Registration::class.java)
            intent.putExtra("firstname", firstname)
            intent.putExtra("lastname", lastname)
            intent.putExtra("gender", gender)
            intent.putExtra("age", age)
            intent.putExtra("selectedConstituency", selectedConstituency)
            intent.putExtra("address", address)

            startActivity(intent)
        }

        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }


    private fun saveDataToFirestore() {
        val firstname = findViewById<EditText>(R.id.editTextFirstName).text.toString()
        val lastname = findViewById<EditText>(R.id.editTextLastName).text.toString()
        val gender = findViewById<EditText>(R.id.editTextGender).text.toString()
        val age = findViewById<EditText>(R.id.editTextAge).text.toString()
        val selectedConstituency = findViewById<Spinner>(R.id.constituencySpinner).selectedItem.toString()
        val address = findViewById<EditText>(R.id.editTextAddresss).text.toString()

        // Retrieve other registration data from input fields...

        val user = hashMapOf(
            "firstname" to firstname,
            "lastname" to lastname,
            "gender" to gender,
            "age" to age,
            "constituency" to selectedConstituency,
            "address" to address
        )

        // Specify the document name (e.g., "registration_data")
        val documentName = "registration_data"

        // Set the data to Firestore with the specified document name
        firestore.collection("users").document(documentName)
            .set(user)
            .addOnSuccessListener {
                Log.d("TAG", "DocumentSnapshot added with ID: $documentName")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this , "Authenticated Successfully" , Toast.LENGTH_SHORT).show()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d("TAG", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
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