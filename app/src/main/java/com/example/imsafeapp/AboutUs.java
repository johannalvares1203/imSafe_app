package com.example.imsafeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.A;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        ImageView goback = findViewById(R.id.backbutton);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                // Lets the user go back to homepage
                startActivity(new Intent(AboutUs.this, Homepage.class));
                finish();
                return true;
            } else if (itemId == R.id.aboutus) {
                startActivity(new Intent(AboutUs.this, AboutUs.class));
                finish();
                return true;
            } else if (itemId == R.id.tips) {
                // Handle Sub Option 1 click
                return true;
            } else if (itemId == R.id.community) {
                // Handle Sub Option 1 click
                return true;
            }
            return false;
        });

        // Set click listener for going back to the homepage
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutUs.this, Homepage.class));
                finish();
            }
        });

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        // Replaces image with one associated with Google Account
        ImageView profileImageView = findViewById(R.id.profileIcon);

        if (account != null) {
            // Load the profile photo using Picasso library (or any other image loading library)
            Picasso.get().load(account.getPhotoUrl()).into(profileImageView);
        } else {
            // If the user is not signed in with Google, you can set a default image or hide the ImageView
            profileImageView.setImageResource(R.drawable.baseline_add_photo_alternate_24);
        }



    }
}