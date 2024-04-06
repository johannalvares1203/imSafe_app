package com.example.imsafeapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.imsafeapp.counsellor.Counsellor_Homepage
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val CHANNEL_ID = "AdminChannel"
    private val CHANNEL_NAME = "Admin Channel"
    private val CHANNEL_DESCRIPTION = "Notification for Admin Login"

    @android.annotation.SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()

        val Mock: android.widget.ImageView = findViewById(R.id.mock)
        val PhoneNoButton: android.widget.ImageView = findViewById(R.id.phone_no)
        val Google: android.widget.ImageView = findViewById(R.id.google_btn)
        val loginButton: android.widget.Button = findViewById(R.id.user_login)
        val emailEditText: android.widget.EditText = findViewById(R.id.Email)
        val passwordEditText: android.widget.EditText = findViewById(R.id.Password)
        val forgotPassword: android.widget.TextView = findViewById(R.id.ForgotPassword)
        val GotoRegistration: android.widget.TextView = findViewById(R.id.loginLink)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1017782215736-m30s5ha0caa7ghc754jd9ia84jvde3op.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Hardcoded admin credentials
                val adminEmail = "admin@gmail.com"
                val adminPassword = "Admin@123"

                // Hardcoded counselor credentials
                val counselorEmail = "counselor"
                val counselorPassword = "counselor"

                if (email == adminEmail && password == adminPassword) {
                    // Admin login, start the admin activity or intent
                    val adminIntent = android.content.Intent(this, Counsellor_Homepage::class.java)
                    startActivity(adminIntent)

                    // Display notification for admin login
                    showAdminNotification()

                    // Clear the email and password fields
                    emailEditText.text.clear()
                    passwordEditText.text.clear()
                } else if (email == counselorEmail && password == counselorPassword) {
                    // Counselor login, start the counselor activity or intent
                    val counselorIntent = android.content.Intent(this, Counsellor_Homepage::class.java)
                    startActivity(counselorIntent)

                    // Display notification for counselor login
                    showCounselorNotification()

                    // Clear the email and password fields
                    emailEditText.text.clear()
                    passwordEditText.text.clear()
                } else {
                    // Regular user login flow
                    signInWithEmailAndPassword(email, password)
                }
            } else {
                android.widget.Toast.makeText(this, "Empty Fields Are not Allowed !!", android.widget.Toast.LENGTH_SHORT).show()
            }
        }


        PhoneNoButton.setOnClickListener {
            val intent = android.content.Intent(this, PhoneNumber::class.java)
            startActivity(intent)
            finish()
        }

        GotoRegistration.setOnClickListener {
            val intent = android.content.Intent(this, Part1_Registration::class.java)
            startActivity(intent)
            finish()
        }

        forgotPassword.setOnClickListener {
            val intent = android.content.Intent(this, Forgot_Password::class.java)
            startActivity(intent)
            finish()
        }


        Mock.setOnClickListener {
            val intent = android.content.Intent(this, Homepage::class.java)
            startActivity(intent)
            finish()
        }

        Google.setOnClickListener {
            signInGoogle()
        }
    }


    private fun showCounselorNotification() {
        // You can use NotificationManager to display notifications
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel for devices running Android Oreo (API 26) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "counselor_channel_id",
                "Counselor Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Create a notification
        val notificationBuilder = NotificationCompat.Builder(this, "counselor_channel_id")
            .setSmallIcon(R.drawable.ic_launcher_background) // Set your small icon here
            .setContentTitle("Counselor Login") // Set your notification title
            .setContentText("You have logged in as a counselor.") // Set your notification message
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Show the notification
        notificationManager.notify(1, notificationBuilder.build())
    }


    private fun signInWithEmailAndPassword(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    user?.let {
                        navigateToHomepage(it.email ?: "")
                    }
                } else {
                    android.widget.Toast.makeText(this, "Authentication Failed.", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun showAdminNotification() {
        // Create a notification channel for Android Oreo and higher
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                android.app.NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_DESCRIPTION
            val notificationManager =
                getSystemService(android.content.Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notificationBuilder = androidx.core.app.NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Admin Login Successful")
            .setContentText("You are now logged in as an admin.")
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_DEFAULT)

        // Display the notification
        val notificationManager = androidx.core.app.NotificationManagerCompat.from(this)
        if (androidx.core.app.ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(1, notificationBuilder.build())
    }

    private fun navigateToHomepage(email: String) {
        val intent = android.content.Intent(this, Homepage::class.java)
        intent.putExtra("USER_EMAIL", email)
        startActivity(intent)
        finish()
    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful) {
                val account: GoogleSignInAccount? = task.result
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            navigateToHomepage(account.email ?: "")
                        } else {
                            android.widget.Toast.makeText(this, "Firebase Authentication failed", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                android.widget.Toast.makeText(this, "Google Sign-In failed", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }



}
