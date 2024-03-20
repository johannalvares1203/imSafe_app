package com.example.imsafeapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class Part2_Registration : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var auth : FirebaseAuth
    private lateinit var number : String
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var selectedImageView: ImageView
    private var selectedImageUri: Uri? = null

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var confirm_password: String
    private lateinit var phone: String


    private lateinit var databaseReference: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_part2_registration)

        firestore = FirebaseFirestore.getInstance()

        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        val countryCodeSpinner: Spinner = findViewById(R.id.countryCodeSpinner)
        val continue_2: Button = findViewById(R.id.buttonRegister2)
        val email: EditText = findViewById(R.id.Email)
        val password: EditText = findViewById(R.id.Password)
        val confirm_pass = findViewById<EditText>(R.id.ConfirmPassword)
        val SendOTP: Button = findViewById(R.id.sendOTPBtn)
        val VerifyOTP: Button = findViewById(R.id.sendOTPBtn)
        val PhoneNumber: EditText = findViewById(R.id.phoneNumber)
        val loginLink = findViewById<TextView>(R.id.buttonRegister2)
        auth = FirebaseAuth.getInstance()

        // For the country code population
        val countryCodes = arrayOf(
            "+1", "+1-684", "+1-767", "+1-784", "+1-787", "+1-809", "+1-829", "+1-849", "+1-868", "+1-869", "+1-876", "+1-939", "+20", "+211", "+212", "+213", "+216", "+218", "+220",
            "+221", "+222", "+223", "+224", "+225", "+226", "+227", "+228", "+229", "+230", "+231", "+232", "+233", "+234", "+235", "+236", "+237", "+238", "+239", "+240", "+241", "+242",
            "+243", "+244", "+245", "+246", "+247", "+248", "+249", "+250", "+251", "+252", "+253", "+254", "+255", "+256", "+257", "+258", "+260", "+261", "+262", "+263", "+264", "+265",
            "+266", "+267", "+268", "+269", "+27", "+290", "+291", "+297", "+298", "+299", "+30", "+31", "+32", "+33", "+34", "+350", "+351", "+352", "+353", "+354", "+355", "+356",
            "+357", "+358", "+359", "+36", "+370", "+371", "+372", "+373", "+374", "+375", "+376", "+377", "+378", "+379", "+380", "+381", "+382", "+383", "+386", "+387", "+389",
            "+39", "+420", "+421", "+423", "+44", "+44-1481", "+44-1534", "+44-1624", "+44-1624", "+45", "+46", "+47", "+48", "+49", "+500", "+501", "+502", "+503", "+504", "+505",
            "+506", "+507", "+508", "+509", "+51", "+52", "+53", "+54", "+55", "+56", "+57", "+58", "+590", "+591", "+592", "+593", "+594", "+595", "+596", "+597", "+598", "+599",
            "+599", "+60", "+61", "+61", "+61", "+62", "+63", "+64", "+65", "+66", "+670", "+672", "+673", "+674", "+675", "+676", "+677", "+678", "+679", "+680", "+681", "+682",
            "+683", "+685", "+686", "+687", "+688", "+689", "+690", "+691", "+692", "+7", "+7", "+81", "+82", "+850", "+852", "+853", "+855", "+856", "+86", "+870", "+880", "+881",
            "+886", "+90", "+91", "+92", "+93", "+94", "+95", "+960", "+961", "+962", "+963", "+964", "+965", "+966", "+967", "+968", "+970", "+971", "+972", "+973", "+974", "+975",
            "+976", "+977", "+98", "+992", "+993", "+994", "+995", "+996", "+998", "+999"
        )


        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countryCodes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        countryCodeSpinner.adapter = adapter


        //Verifications for EmailId & Password
        continue_2.setOnClickListener {
            registerUser(PhoneNumber.text.toString(), email.text.toString(), password.text.toString())
            saveDataToFirestore()

            val intent = Intent(this, Part3_Registration::class.java)
            startActivity(intent)
            finish()
//            vibrator.vibrate(10000)
//            val email = email.text.toString().trim()
//            val password = password.text.toString().trim()
//            val confirmPassInput = confirm_pass.text.toString().trim()
//
//            when {
//                email.isEmpty() || password.isEmpty() || confirmPassInput.isEmpty() -> {
//                    Toast.makeText(this, "Empty Fields Are not Allowed!!", Toast.LENGTH_SHORT).show()
//                }
//                password != confirmPassInput -> {
//                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
//                }
//                !isValidEmail(email) -> {
//                    Toast.makeText(this, "Invalid Email Format", Toast.LENGTH_SHORT).show()
//                }
//                password.length <= 6 -> {
//                    Toast.makeText(this, "Password should be greater than 6 characters", Toast.LENGTH_SHORT).show()
//                }
//                !isPasswordComplex(password) -> {
//                    Toast.makeText(this, "Password should contain letters, symbols, and numbers", Toast.LENGTH_SHORT).show()
//                }
//                else -> {
//                    // Registration conditions met, navigate to Homepage
//                    val intent = Intent(this, Homepage::class.java)
//                    // Pass the email to the Homepage activity
//                    intent.putExtra("USER_EMAIL", email)
//                    startActivity(intent)
//                    finish() // Finish the current activity to remove it from the stack
//                }
//            }
        }

        //Verifications for Phone Numbers
        SendOTP.setOnClickListener {
            number = PhoneNumber.text.trim().toString()
            if (number.isNotEmpty()) {
                if (number.length == 10) {
                    number = "+91$number"
                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                    // Show a toast message indicating that OTP is sent
                    Toast.makeText(this, "OTP Sent", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Please Enter correct Number", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please Enter Number", Toast.LENGTH_SHORT).show()
            }
            vibrator.vibrate(1000)
        }
    }

    private fun registerUser(userName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    val userId: String = user!!.uid

                    databaseReference =
                        FirebaseDatabase.getInstance().getReference("Users").child(userId)

                    val hashMap: HashMap<String, String> = HashMap()
                    hashMap.put("userId", userId)
                    hashMap.put("userName", userName)
                    hashMap.put("profileImage", "")

                    databaseReference.setValue(hashMap).addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            //open home activity
                            /*etName.setText("")
                            etEmail.setText("")
                            etPassword.setText("")
                            etConfirmPassword.setText("")*/
                            val intent = Intent(
                                this@Part2_Registration,
                                Part3_Registration::class.java
                            )
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
    }



    private fun saveDataToFirestore() {
        // Collect registration data from Part 1
        val email = findViewById<EditText>(R.id.Email).text.toString()
        val password = findViewById<EditText>(R.id.Password).text.toString()
        val confirm_password = findViewById<EditText>(R.id.ConfirmPassword).text.toString()
        val phone = findViewById<EditText>(R.id.phoneNumber).text.toString()
        // Retrieve other registration data from input fields...

        // Create a map with the additional data you want to add
        val additionalUserData = hashMapOf<String, Any>(
            "email" to email,
            "password" to password,
            "confirm_password" to confirm_password,
            "phone" to phone
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



    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this , "Authenticate Successfully" , Toast.LENGTH_SHORT).show()
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


    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.d("TAG", "onVerificationFailed: ${e.toString()}")
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.d("TAG", "onVerificationFailed: ${e.toString()}")
            }
            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            // Save verification ID and resending token so we can use them later


        }

    }

    private fun isValidEmail(email: String): Boolean {
        // Perform email validation using a regex pattern
        val emailPattern = Patterns.EMAIL_ADDRESS
        return emailPattern.matcher(email).matches()
    }

    private fun isPasswordComplex(password: String): Boolean {
        val letter = Pattern.compile("[a-zA-z]")
        val digit = Pattern.compile("[0-9]")
        val specialChar = Pattern.compile("[!@#$%^&*()]")
        return letter.matcher(password).find() && digit.matcher(password).find() && specialChar.matcher(password).find()
    }
}