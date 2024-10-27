package com.example.shopappp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopappp.databinding.FragmentCartBinding
import com.example.shopappp.models.CartItem
import com.example.shopappp.services.CartService

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var cartService: CartService
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartService = CartService(requireContext())
        setupRecyclerView()
        loadCartItems()
        updateTotals()

        binding.checkoutButton.setOnClickListener {
            val intent = Intent(requireContext(), CheckoutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            onQuantityChanged = { item, newQuantity ->
                cartService.updateQuantity(item, newQuantity)
                loadCartItems() // Reload cart items after quantity change
                updateTotals()
            },
            onItemRemoved = { item ->
                cartService.removeFromCart(item)
                loadCartItems()
                updateTotals()
            }
        )
        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
        }
    }

    private fun loadCartItems() {
        val cartItems = cartService.getCartItems()
        cartAdapter.submitList(cartItems)
    }

    private fun updateTotals() {
        val cartItems = cartService.getCartItems()
        val subtotal = cartItems.sumOf { it.price * it.quantity }
        val discount = calculateDiscount(subtotal)
        val total = subtotal - discount

        binding.subtotalTextView.text = "Sub total: RS.${String.format("%.2f", subtotal)}"
        binding.discountTextView.text = "Discount: RS.${String.format("%.2f", discount)}"
        binding.totalTextView.text = "Total: RS.${String.format("%.2f", total)}"
    }

    private fun calculateDiscount(subtotal: Double): Double {
        // Implement your discount logic here
        return 2.00 // For now, we'll use a fixed discount of 2.00
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
