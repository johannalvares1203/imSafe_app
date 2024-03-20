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
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
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



        //Verifications for EmailId & Password
        complete_registration.setOnClickListener {

            saveDataToFirestore()

            val intent = Intent(this, Homepage::class.java)
            startActivity(intent)
            finish()
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
