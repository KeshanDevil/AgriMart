package com.example.shopappp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton

class OnboardingActivityOne : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_one)

        // Find the next button
        val nextButton: ImageButton = findViewById(R.id.buttonNext)

        // Set click listener for the next button
        nextButton.setOnClickListener {
            // Navigate to OnboardingActivityTwo
            val intent = Intent(this, OnBoardingActivityTwo::class.java)
            startActivity(intent)
        }
    }
}