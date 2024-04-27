package com.example.imsafeapp

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException

class Record_Audio : AppCompatActivity() {

    private lateinit var recordButton: Button
    private lateinit var stopButton: Button

    private var mediaRecorder: MediaRecorder? = null
    private var outputFile: String? = null

    private val RECORD_AUDIO_PERMISSION_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_audio)

        recordButton = findViewById(R.id.record_button)
        stopButton = findViewById(R.id.stop_button)

        // Output file in external storage
        outputFile = "${Environment.getExternalStorageDirectory().absolutePath}/myrecording.3gp"

        recordButton.setOnClickListener { startRecording() }
        stopButton.setOnClickListener { stopRecording() }
    }

    private fun startRecording() {
        if (checkAndRequestPermissions()) {
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setOutputFile(outputFile)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

                try {
                    prepare()
                    start()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            recordButton.isEnabled = false
            stopButton.isEnabled = true
        }
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            try {
                stop()
            } catch (e: IllegalStateException) {
                // Handle the exception if MediaRecorder is not in a valid state
                e.printStackTrace()
            }
            release()
        }
        mediaRecorder = null

        recordButton.isEnabled = true
        stopButton.isEnabled = false
    }


    private fun checkAndRequestPermissions(): Boolean {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)

        return if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), RECORD_AUDIO_PERMISSION_CODE)
            false
        } else {
            true
        }
    }
}
