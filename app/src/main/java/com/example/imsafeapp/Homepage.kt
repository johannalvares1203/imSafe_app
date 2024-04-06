package com.example.imsafeapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.imsafeapp.chat.activity.UsersActivity
import com.example.imsafeapp.community.admin.RequestsActivity
import com.example.imsafeapp.community.user.CommunityActivity
import com.example.imsafeapp.community.user.CommunityFeedActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class Homepage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var countdownTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Homepage", "onCreate called")
        setContentView(R.layout.activity_homepage)

        val police: Button = findViewById(R.id.reportinc)
        val chatwithcounsellor: Button = findViewById(R.id.chat)

        val redirect: ImageView = findViewById(R.id.profileIcon)

        val account = GoogleSignIn.getLastSignedInAccount(this)
        //Replaces image with one associated with Google Account
        val profileImageView: ImageView = findViewById(R.id.profileIcon)

        if (account != null) {
            // Load the profile photo using Picasso library (or any other image loading library)
            Picasso.get().load(account.photoUrl).into(profileImageView)
        } else {
            // If the user is not signed in with Google, you can set a default image or hide the ImageView
            profileImageView.setImageResource(R.drawable.baseline_add_photo_alternate_24)
        }

        redirect.setOnClickListener {
            startActivity(Intent(this, Profile_Settings::class.java))
            finish()
        }

        chatwithcounsellor.setOnClickListener {
            startActivity(Intent(this, UsersActivity::class.java))
            finish()
        }

        police.setOnClickListener {
            openPanicActivity()
        }

        // for bottom menu
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    //Lets the user go back to homepage
                        startActivity(Intent(this, Homepage::class.java))
                        finish()
                    true
                }
                R.id.aboutus -> {
                    val intent = Intent(this, AboutUs::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.tips -> {
                    // Handle Sub Option 1 click
                    true
                }
                R.id.community -> {
                    startActivity(Intent(this@Homepage, RequestsActivity::class.java))

                    /*val userId = FirebaseAuth.getInstance().currentUser?.uid
                    val requestRef = FirebaseDatabase.getInstance().getReference("Requests").child(userId!!)
                    requestRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val approved = dataSnapshot.child("approved").value as? Boolean?: false
                            if (approved) {
                                startActivity(Intent(this@Homepage, CommunityFeedActivity::class.java))
                            } else {
                                startActivity(Intent(this@Homepage, CommunityActivity::class.java))
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            //possible errors
                        }
                    })*/
                    true
                }
                else -> false
            }
        }

        auth = FirebaseAuth.getInstance()

        val userEmail = intent.getStringExtra("USER_EMAIL")
        val displayName = intent.getStringExtra("name")


//        findViewById<Button>(R.id.reportIncident).setOnClickListener {
//            val intent = Intent(this, Report_Incident::class.java)
//            startActivity(intent)
//        }

//        val userEmailTextView: TextView = findViewById(R.id.userEmailTextView)
//        userEmailTextView.text = "Welcome, $userEmail!"

    }




    private fun openPanicActivity() {
        val intent = Intent(this, PanicButton::class.java)
        startActivityForResult(intent, PANIC_ACTIVITY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PANIC_ACTIVITY_REQUEST_CODE) {
            // Handle the result if needed
            // For example, you can check resultCode or data to see if the panic action was cancelled
        }
    }

    companion object {
        private const val PANIC_ACTIVITY_REQUEST_CODE = 1001
    }

    private fun signOutGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleSignInClient.signOut().addOnCompleteListener(this) {
            // Handle Google Sign Out completion if needed
        }
    }
}
