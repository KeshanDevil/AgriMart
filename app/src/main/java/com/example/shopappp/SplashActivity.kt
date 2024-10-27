package com.example.shopappp

import android.content.Intent
import android.os.Bundle
import android.util.Log // Import Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.shopappp.MainActivity
import com.example.shopappp.services.AuthService

class SplashActivity : AppCompatActivity() {
    private val TAG = "SplashActivity" // Tag for Logcat
var authService = AuthService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        // Handle system bars insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get reference to the Get Started button
        val getStartedButton = findViewById<AppCompatButton>(R.id.getStartedButton)

        // Set OnClickListener to navigate to OnboardActivity1 and log the click
        getStartedButton.setOnClickListener {
            Log.d(TAG, "Get Started button clicked!") // Log the click

            val intent = Intent(this, OnboardingActivityOne::class.java)
            startActivity(intent)
        }
        if(authService.getCurrentUser()!=null){
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
