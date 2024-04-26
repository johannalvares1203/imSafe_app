package com.example.imsafeapp

import android.app.ProgressDialog
import android.content.Intent
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.imsafeapp.Homepage
import com.example.imsafeapp.R
import com.example.imsafeapp.databinding.ActivityAddMediaBinding
import com.example.imsafeapp.databinding.ActivityMain1Binding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddMedia : AppCompatActivity() {

    private lateinit var binding: ActivityAddMediaBinding
    private var imageUri: Uri? = null
    private lateinit var storageReference: StorageReference
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recordPhoto.setOnClickListener {
            selectImage()
        }

        binding.uploadImageButton.setOnClickListener {
            uploadImage()
        }

        binding.submitButton.setOnClickListener {
            // Call sendImageViaSMS when submit button is clicked
            if (imageUri != null) {
                sendImageViaSMS(imageUri.toString())
            } else {
                Toast.makeText(this, "Please upload an image first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImage() {
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading File....")
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA)
        val now = Date()
        val fileName = formatter.format(now)
        storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

        storageReference.putFile(imageUri!!)
            .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                binding.firebaseImage.setImageURI(null)
                Toast.makeText(this@AddMedia, "Successfully Uploaded", Toast.LENGTH_SHORT)
                    .show()
                if (progressDialog.isShowing) progressDialog.dismiss()
            })
            .addOnFailureListener(OnFailureListener { e ->
                if (progressDialog.isShowing) progressDialog.dismiss()
                Toast.makeText(this@AddMedia, "Failed to Upload", Toast.LENGTH_SHORT).show()
            })
    }

    private fun sendImageViaSMS(imageUri: String) {
        // Replace "phoneNumber" with the recipient's phone number
        val phoneNumber = "8767202131"

        // Replace "Your SMS Body" with the desired message body
        val smsBody = "Here is the image: $imageUri"

        // Create an intent to send SMS
        val smsIntent = Intent(Intent.ACTION_VIEW)
        smsIntent.data = Uri.parse("sms:$phoneNumber")
        smsIntent.putExtra("sms_body", smsBody)

        // Check if the device supports sending SMS
        if (smsIntent.resolveActivity(packageManager) != null) {
            startActivity(smsIntent)
        } else {
            Toast.makeText(this, "SMS sending not supported on this device", Toast.LENGTH_SHORT).show()
        }
    }



    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            binding.firebaseImage.setImageURI(imageUri)
        }
    }
}

