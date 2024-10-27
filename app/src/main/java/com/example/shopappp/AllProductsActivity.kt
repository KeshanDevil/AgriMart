package com.example.shopappp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopappp.models.Product
import com.example.shopappp.services.CartService
import com.example.shopappp.services.ProductService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch

class AllProductsActivity : AppCompatActivity() {

    private lateinit var productAdapter: ProductAdapter
    private lateinit var rvProducts: RecyclerView
    private var firestoreListener: ListenerRegistration? = null
    private lateinit var  cartService: CartService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_all_products)

        // Adjust window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize RecyclerView
        rvProducts = findViewById(R.id.rvProducts)
        rvProducts.layoutManager = LinearLayoutManager(this)
        cartService  = CartService(this)
        // Initialize adapter
        productAdapter = ProductAdapter(listOf(),cartService)
        rvProducts.adapter = productAdapter

        // Start listening to Firestore products collection
        listenToProducts()
    }

    private fun listenToProducts() {
        val db = FirebaseFirestore.getInstance()
        firestoreListener = db.collection("inventory")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null || snapshot == null) {
                    // Handle errors
                    return@addSnapshotListener
                }

                val products = snapshot.documents.mapNotNull { it.toObject(Product::class.java) }
                productAdapter.updateProducts(products)
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        firestoreListener?.remove()  // Stop listening to Firestore when activity is destroyed
    }
}
