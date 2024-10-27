package com.example.shopappp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shopappp.models.CartItem
import com.example.shopappp.models.Order
import com.example.shopappp.services.CartService
import com.example.shopappp.services.OrderService

class CheckoutActivity : AppCompatActivity() {

    private lateinit var orderService: OrderService
    private lateinit var cartService: CartService
    private lateinit var cartItems: List<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        orderService = OrderService(this)
        cartService = CartService(this)
        cartItems = cartService.getCartItems()

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val addressInput = findViewById<EditText>(R.id.addressInput)
        val contactInput = findViewById<EditText>(R.id.contactInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val placeOrderButton = findViewById<Button>(R.id.placeOrderButton)

        placeOrderButton.setOnClickListener {
            val name = nameInput.text.toString()
            val address = addressInput.text.toString()
            val contactNumber = contactInput.text.toString()
            val email = emailInput.text.toString()

            if (name.isEmpty() || address.isEmpty() || contactNumber.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val totalAmount = cartItems.sumOf { it.price * it.quantity }
                val order = Order(
                    name = name,
                    address = address,
                    contactNumber = contactNumber,
                    email = email,
                    totalAmount = totalAmount,
                    items = cartItems
                )

                orderService.placeOrder(order)
                orderService.clearCart()
                Toast.makeText(this, "Order placed successfully", Toast.LENGTH_LONG).show()

                finish() // Navigate back to cart
            }
        }
    }
}
