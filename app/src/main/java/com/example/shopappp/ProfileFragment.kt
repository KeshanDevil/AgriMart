package com.example.shopappp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.shopappp.databinding.FragmentProfileBinding
import com.example.shopappp.models.AppUser
import com.example.shopappp.services.AuthService
import com.example.shopappp.services.CurrentUserService
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var authService = AuthService()
    private lateinit var userService: CurrentUserService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userService = CurrentUserService(requireContext())

        val userId = "currentUserId"
        checkUserProfile(userId)

        binding.saveProfileButton.setOnClickListener {
            saveUserProfile(userId)
        }
        binding.logoutButton.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            authService.signOut()
            var intent = Intent(requireContext(),LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkUserProfile(userId: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            val user = userService.getUser(userId)
            if (user != null) {
                displayUserProfile(user)
            } else {
                clearProfileForm()
                Toast.makeText(context, "No profile found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayUserProfile(user: AppUser) {
        binding.nameEditText.setText(user.name)
        binding.addressEditText.setText(user.address)
        binding.emailEditText.setText(user.email)
        binding.phoneEditText.setText(user.phoneNumber)
    }

    private fun clearProfileForm() {
        binding.nameEditText.setText("")
        binding.addressEditText.setText("")
        binding.emailEditText.setText("")
        binding.phoneEditText.setText("")
    }

    private fun saveUserProfile(userId: String) {
        val user = AppUser(
            id = userId,
            name = binding.nameEditText.text.toString(),
            address = binding.addressEditText.text.toString(),
            email = binding.emailEditText.text.toString(),
            phoneNumber = binding.phoneEditText.text.toString()
        )

        viewLifecycleOwner.lifecycleScope.launch {
            val success = userService.updateUser(user)
            if (success) {
                Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
            } else {
             val s=   userService.createUser(user)
              if(s){
                  Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
              }else{
                  Toast.makeText(context, "Error updating profile", Toast.LENGTH_SHORT).show()
              }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
