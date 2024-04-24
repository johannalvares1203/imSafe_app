package com.example.imsafeapp
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.imsafeapp.R
import com.google.firebase.auth.FirebaseAuth


class Forgot_Password : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val resetButton = findViewById<Button>(R.id.btnResetPassword)
        resetButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.etEmail).text.toString()
            // Implement password reset logic here, e.g., send reset email
            sendPasswordResetEmail(email)
        }
    }

    private fun sendPasswordResetEmail(email: String) {
        // Implement logic to send password reset email
        // This might involve interacting with Firebase Authentication or an external service
        // For example:
         FirebaseAuth.getInstance().sendPasswordResetEmail(email)
             .addOnCompleteListener { task ->
                 if (task.isSuccessful) {
                     // Password reset email sent successfully
                     Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                 } else {
                     // Password reset email sending failed
                     Toast.makeText(this, "Failed to send password reset email", Toast.LENGTH_SHORT).show()
                 }
             }
    }
}
