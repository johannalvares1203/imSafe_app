package com.example.imsafeapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.provider.MediaStore
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Query


class Part3_Registration : AppCompatActivity() {

    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var auth : FirebaseAuth
    private lateinit var number : String
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var selectedImageView: ImageView
    private var selectedImageUri: Uri? = null

    private lateinit var contact1_name: String
    private lateinit var contact1_number: String
    private lateinit var contact2_name: String
    private lateinit var contact2_number: String
    private lateinit var blood_group: String
    private lateinit var uploaded_image: String

    private lateinit var firstname: String
    private lateinit var lastname: String
    private var age: Int = 0
    private lateinit var address: String
    private lateinit var constituency: String
    private lateinit var gender: String

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var confirm_password: String
    private lateinit var phone: String

    private lateinit var databaseReference: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_part3_registration)

        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        val termsAndConditionsLink = findViewById<TextView>(R.id.termsAndConditionsLink)
        val complete_registration: Button = findViewById(R.id.buttonRegister)
        auth = FirebaseAuth.getInstance()

        selectedImageView = findViewById(R.id.selectedImageView)

        val chooseFileBtn: Button = findViewById(R.id.chooseFileBtn)
        chooseFileBtn.setOnClickListener {
            openFileChooser()
        }

        // Terms & Conditions
        termsAndConditionsLink.setOnClickListener {
            // Load terms and conditions from string resource
            val content = getString(R.string.terms_and_conditions)

            // Display terms and conditions content with line breaks
            val formattedContent = Html.fromHtml(content.replace("\n", "<br/>"))
            AlertDialog.Builder(this)
                .setTitle("Terms and Conditions")
                .setMessage(formattedContent)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        complete_registration.isEnabled = false // Disable the button by default

        val checkBox: CheckBox = findViewById(R.id.checkbox)

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            complete_registration.isEnabled = isChecked // Enable or disable the button based on checkbox state
        }

        //Verifications for EmailId & Password
        complete_registration.setOnClickListener {
            saveDataToFirestore()

            Log.d("QWE", "2nd button clicked")
            val firstname = intent.getStringExtra("firstname")
            val lastname = intent.getStringExtra("lastname")
            val email = intent.getStringExtra("email")
            val password = intent.getStringExtra("password")
            val cUserName="$firstname $lastname"
            Log.d("QWE", "$firstname")
            Log.d("QWE", "$email $password $cUserName")

            registerUser(cUserName, email!!, password!!)


            val intent = Intent(this, Homepage::class.java)
            startActivity(intent)
        }


        //For Blood Group
        val bloodGroupEditText: TextInputEditText = findViewById(R.id.BloodGroup)
        bloodGroupEditText.isEnabled = false // Disable the EditText by default

        val checkBox1: CheckBox = findViewById(R.id.includeBloodGroup)

        checkBox1.setOnCheckedChangeListener { _, isChecked ->
            bloodGroupEditText.isEnabled = isChecked // Enable or disable the EditText based on checkbox state
        }


    }

    private fun registerUser(userName: String, email: String, password: String) {
        Log.d("QWE", "inside registerUser()")
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    val userId: String = user!!.uid

                    val contact1_name = findViewById<EditText>(R.id.editTextEmergencyContact1Name).text.toString()
                    val contact1_number = findViewById<EditText>(R.id.editTextEmergencyContact1Number).text.toString()
                    val contact2_name = findViewById<EditText>(R.id.editTextEmergencyContact2Name).text.toString()
                    val contact2_number = findViewById<EditText>(R.id.editTextEmergencyContact2Number).text.toString()
                    val blood_group = findViewById<EditText>(R.id.BloodGroup).text.toString()

                    val firstname = intent.getStringExtra("firstname")
                    val lastname = intent.getStringExtra("lastname")
                    val gender = intent.getStringExtra("gender")
                    val age = intent.getStringExtra("age")
                    val selectedConstituency = intent.getStringExtra("selectedConstituency")
                    val address = intent.getStringExtra("address")
                    val PhoneNumber = intent.getStringExtra("PhoneNumber")


                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)
                    val hashMap: HashMap<String, String> = HashMap()
                    hashMap.put("userId", userId)
                    hashMap.put("profileImage", "")

                    Log.d("QWE", "inside hashmap")

                    hashMap.put("userName", userName)
                    hashMap.put("firstname", firstname!!)
                    hashMap.put("lastname", lastname!!)
                    hashMap.put("gender", gender!!)
                    hashMap.put("age", age!!)
                    hashMap.put("selectedConstituency", selectedConstituency!!)
                    hashMap.put("address", address!!)

                    hashMap.put("email", email)
                    //hashMap.put("password", password)
                    hashMap.put("PhoneNumber", PhoneNumber!!)

                    hashMap.put("contact1_name", contact1_name)
                    hashMap.put("contact1_number", contact1_number)
                    hashMap.put("contact2_name", contact2_name)
                    hashMap.put("contact2_number", contact2_number)
                    hashMap.put("blood_group", blood_group)

                    hashMap.put("role", "user")
                    //hashMap.put("role", "counsellor")
                    //hashMap.put("role", "volunteer")

                    databaseReference.setValue(hashMap).addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            val intent = Intent(this@Part3_Registration, Homepage::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
    }

    private fun saveDataToFirestore() {
        // Collect registration data from Part 1
        val contact1_name = findViewById<EditText>(R.id.editTextEmergencyContact1Name).text.toString()
        val contact1_number = findViewById<EditText>(R.id.editTextEmergencyContact1Number).text.toString()
        val contact2_name = findViewById<EditText>(R.id.editTextEmergencyContact2Name).text.toString()
        val contact2_number = findViewById<EditText>(R.id.editTextEmergencyContact2Number).text.toString()
        val blood_group = findViewById<EditText>(R.id.BloodGroup).text.toString()

        // Create a map with the additional data you want to add
        val additionalUserData = hashMapOf<String, Any>(
            "contact1_name" to contact1_name,
            "contact1_number" to contact1_number,
            "contact2_name" to contact2_name,
            "contact2_number" to contact2_number,
            "blood_group" to blood_group,
        )

        // Specify the document name (e.g., "registration_data")
        val documentName = "registration_data"

        // Merge the additional data with the existing document data
        firestore.collection("users").document(documentName)
            .set(additionalUserData, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("TAG", "Additional data added to document: $documentName")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding additional data to document", e)
            }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            displaySelectedImage()
        }
    }

    private fun displaySelectedImage() {
        try {
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
            selectedImageView.setImageBitmap(bitmap)
            selectedImageView.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}