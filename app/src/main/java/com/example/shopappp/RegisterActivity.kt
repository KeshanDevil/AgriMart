package com.example.shopappp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.shopappp.MainActivity
import com.example.shopappp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    // Tag for logcat logging
    private val TAG = "RegisterActivity"

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Log when "Register" button is clicked
        binding.registerButton.setOnClickListener {
            Log.d(TAG, "Register button clicked") // Log register button click
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            // Check if all fields are filled
            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                // Check if passwords match
                if (password == confirmPassword) {
                    // Check if terms are agreed upon
                    if (binding.termsCheckBox.isChecked) {
                        Log.d(TAG, "All input is valid, starting registration") // Log when inputs are valid
                        registerUser(email, password)

                    } else {
                        Toast.makeText(this, "Please agree to the Terms and Conditions", Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "Terms and conditions not accepted") // Log error when terms not checked
                    }
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Passwords do not match") // Log error when passwords don't match
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "One or more fields are empty") // Log error when fields are empty
            }
        }
    }

    // Firebase registration logic
    private fun registerUser(email: String, password: String) {
        Log.d(TAG, "Registering user with email: $email") // Log user registration attempt
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Registration successful") // Log successful registration
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                    // Navigate to MainActivity or next screen
                    var intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.e(TAG, "Registration failed: ${task.exception?.message}") // Log error on registration failure
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
