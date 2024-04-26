package com.example.imsafeapp

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.telephony.SmsManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso


class PanicButton : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener  {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap
    private lateinit var countdownTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panic_button)


        // for bottom menu
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    //Lets the user go back to homepage
                    startActivity(Intent(this, Homepage::class.java))
                    true
                }
                R.id.aboutus -> {
                    val intent = Intent(this, AboutUs::class.java)
                    startActivity(intent)
                    true
                }
                R.id.tips -> {
                    // Handle Sub Option 1 click
                    true
                }
                R.id.community -> {
                    // Handle Sub Option 1 click
                    true
                }
                else -> false
            }
        }

        // Find the MaterialToolbar by its ID
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Set the title for the toolbar
        val locationInfoTextView: TextView = findViewById(R.id.locationInfoTextView)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment

        mapFragment.getMapAsync(this)
        locationInfoTextView.visibility = View.GONE // Hide initially

        val fabNavigate: FloatingActionButton = findViewById(R.id.fabNavigate)
        fabNavigate.setOnClickListener {
            // Handle the click event for navigating to another activity
            navigateToAnotherActivity()
        }



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
    }

    // Inside PanicButton activity code

    private fun handlePanicButtonClick() {
        // Request location permissions
        if (checkLocationPermission()) {
            // Vibrate for 7 seconds
            checkSmsPermissionAndSendSms()
            vibrateForSeconds(3)

            // Wait for 7 seconds before triggering emergency actions
            Handler(Looper.getMainLooper()).postDelayed({
                // Get current location and display on map
                getCurrentLocationAndDisplayOnMap()

                // Send location, call, and SMS
                sendEmergencyAlerts()

                // Show the map and other elements
                val locationInfoTextView: TextView = findViewById(R.id.locationInfoTextView)
                locationInfoTextView.visibility = View.VISIBLE
                val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
                mapFragment.view?.visibility = View.VISIBLE

                // Start a short vibration sequence and notify the user
                startShortVibrationAndNotify()
            }, 4000)
        }
    }


    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap
        googleMap.uiSettings.isZoomControlsEnabled = true

        // Set the OnMarkerClickListener
        googleMap.setOnMarkerClickListener(this)

        handlePanicButtonClick()
    }


    private fun getCurrentLocationAndDisplayOnMap() {
        // Get current location
        getCurrentLocation { location ->
            // Display the user's exact location on the map
            val userLocation = LatLng(location.latitude, location.longitude)
            googleMap.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17f)) // Zoom a bit closer to make the circle more visible

            // Draw a circle around the location to represent accuracy
            val circleOptions = CircleOptions()
                .center(userLocation)
                .radius(15.0)
                .strokeColor(Color.RED)
                .fillColor(0x30ff0000) // Fill color of the circle
                .strokeWidth(2f) // Border width of the circle
            googleMap.addCircle(circleOptions)
        }
    }


    @SuppressLint("MissingPermission")
    private fun startShortVibrationAndNotify() {
        // Start a short vibration sequence for 5 seconds
        vibrateForSeconds(2)
        createNotificationChannel()

        // Continue with notification
        val notificationBuilder = NotificationCompat.Builder(this, "EmergencyChannel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Panic Button Clicked")
            .setContentText("Emergency alert sent. Help is on the way!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setColor(Color.RED)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(1, notificationBuilder.build())
        }
    }



    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Emergency Channel"
            val descriptionText = "Channel for emergency notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("EmergencyChannel", name, importance).apply {
                description = descriptionText
            }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return false
        }
        return true
    }

    private fun getCurrentLocation(callback: (Location) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
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
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                callback(it)
            }
        }
    }


    private fun checkSmsPermissionAndSendSms() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), SMS_PERMISSION_REQUEST_CODE)
        } else {
            // Permission already granted, proceed with sending SMS
            sendEmergencyAlerts()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with sending SMS
                sendEmergencyAlerts()
            } else {
                // Permission denied, inform the user or handle the situation accordingly
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendEmergencyAlerts() {
        // Emergency contacts
        val emergencyContacts = arrayOf("8767202131", "8767202131") // Update with real numbers

        // Get the last known location
        getCurrentLocation { location ->
            val message = "Emergency! I need help. This is my location, accurate to 15 meters:: http://maps.google.com/?q=${location.latitude},${location.longitude}"

            sendSMS(emergencyContacts, message)

            // Optionally, make a phone call (ensure you handle permissions and user consent appropriately)
            makeEmergencyCall("8767202131") // Replace with an actual emergency number or remove if not needed
        }
    }

    private fun makeEmergencyCall(phoneNumber: String) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$phoneNumber")
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent)
        } else {
            // Request permission or inform the user they haven't granted it
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), CALL_PERMISSION_REQUEST_CODE)
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val CALL_PERMISSION_REQUEST_CODE = 2
        private const val SMS_PERMISSION_REQUEST_CODE = 3

    }

    private fun sendSMS(phoneNumbers: Array<String>, message: String) {
        val smsManager = SmsManager.getDefault()
        phoneNumbers.forEach {
            smsManager.sendTextMessage(it, null, message, null, null)
        }
    }

    private fun vibrateForSeconds(seconds: Int) {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val vibrationEffect = VibrationEffect.createOneShot(seconds * 1000L, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(seconds * 1000L)
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        if (marker != null) {
            // Extract coordinates from the clicked marker
            val clickedMarkerLatLng = marker.position
            val latitude = clickedMarkerLatLng.latitude
            val longitude = clickedMarkerLatLng.longitude

            // Show the coordinates (you can use a Toast or any other UI element)
            Toast.makeText(
                this,
                "Clicked Marker Coordinates: Latitude=$latitude, Longitude=$longitude",
                Toast.LENGTH_SHORT
            ).show()

            // Return true to indicate that the click event is handled
            return true
        }

        // Return false to indicate that the click event is not handled
        return false
    }

    fun navigateToAnotherActivity() {
        val intent = Intent(this, AddMedia::class.java)
        startActivity(intent)
    }

}