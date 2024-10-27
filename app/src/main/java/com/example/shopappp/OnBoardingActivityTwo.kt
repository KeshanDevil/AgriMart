package com.example.shopappp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class OnBoardingActivityTwo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_on_boarding_two)

        // Find the next button
        val nextButton: FloatingActionButton = findViewById(R.id.buttonNext)

        // Set click listener for the next button
        nextButton.setOnClickListener {
            // Navigate to OnBoardingActivityThree
            val intent = Intent(this, OnboardingActivityThree::class.java)
            startActivity(intent)
        }
    }
}