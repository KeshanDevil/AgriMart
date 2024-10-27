package com.example.shopappp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.shopappp.services.AuthService
import com.google.android.material.textfield.TextInputEditText
import androidx.lifecycle.lifecycleScope
import com.example.shopappp.MainActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    // Tag for logging
    private val TAG = "LoginActivity"
    var authService = AuthService() // Assuming AuthService is correctly defined elsewhere

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Setup buttons
        val loginButton: Button = findViewById(R.id.btnLogin)
        val fbLoginButton: Button = findViewById(R.id.btnFacebook)
        val googleLoginButton: Button = findViewById(R.id.btnGoogle)
        val registerTextView: TextView = findViewById(R.id.tvRegister) // Register text view

        // Get email and password input fields
        val emailInput: TextInputEditText = findViewById(R.id.etEmail)
        val passwordInput: TextInputEditText = findViewById(R.id.etPassword)

        // Set click listeners for login methods
        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            // Launching a coroutine in the lifecycleScope to call the suspend function
            lifecycleScope.launch {
                handleLogin(email, password)
            }
        }

        fbLoginButton.setOnClickListener {
            handleFbLogin()
        }

        googleLoginButton.setOnClickListener {
            handleGoogleLogin()
        }

        // Set click listener for Register text
        registerTextView.setOnClickListener {
            navigateToRegister()
        }
    }

    // Method for handling normal login
    private suspend fun handleLogin(email: String, password: String) {
        // Your login logic goes here
        try {
            authService.signInWithEmailAndPassword(email, password)
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }catch (e: Exception) {
            // Handle login failure and show error message
            Toast.makeText(this, "Login failed: ${e.message}", Toast.LENGTH_LONG).show()
        }

    }

    // Method for handling Facebook login
    private fun handleFbLogin() {
        Log.d(TAG, "Facebook login button clicked")
        // Add Facebook login logic here
    }

    // Method for handling Google login
    private fun handleGoogleLogin() {
        Log.d(TAG, "Google login button clicked")
        // Add Google login logic here (e.g., Google Sign-In API integration)
    }

    // Method to navigate to Register Activity
    private fun navigateToRegister() {
        Log.d(TAG, "Register text clicked")
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
