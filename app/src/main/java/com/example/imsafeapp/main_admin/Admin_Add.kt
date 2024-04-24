package com.example.imsafeapp.main_admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.imsafeapp.LoginActivity
import com.example.imsafeapp.R
import com.example.imsafeapp.databinding.ActivityAdminAddBinding
import com.example.imsafeapp.databinding.ActivityMainBinding

class Admin_Add : AppCompatActivity() {

    private lateinit var binding: ActivityAdminAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mainUpload.setOnClickListener(View.OnClickListener{
            val intent = Intent(this@Admin_Add, Admin_Upload::class.java)
            startActivity(intent)
            finish()
        })
        binding.mainUpdate.setOnClickListener(View.OnClickListener{
            val intent = Intent(this@Admin_Add, Admin_Update::class.java)
            startActivity(intent)
        })
        binding.mainDelete.setOnClickListener(View.OnClickListener{
            val intent = Intent(this@Admin_Add, Admin_Delete::class.java)
            startActivity(intent)
        })

        binding.loginLink.setOnClickListener(View.OnClickListener{
            val intent = Intent(this@Admin_Add, LoginActivity::class.java)
            startActivity(intent)
        })

        binding.viewdata.setOnClickListener(View.OnClickListener{
            val intent = Intent(this@Admin_Add, ViewDetails::class.java)
            startActivity(intent)
        })
    }
}