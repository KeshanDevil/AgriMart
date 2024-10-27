package com.example.shopappp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopappp.models.CartItem
import com.example.shopappp.models.Product
import com.example.shopappp.services.CartService

class ProductAdapter(private var products: List<Product>,private var cartService: CartService) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {



    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.tvProductName)
        val priceTextView: TextView = itemView.findViewById(R.id.tvProductPrice)
        val productImageView: ImageView = itemView.findViewById(R.id.ivProductImage)
        val addToCartButton: ImageButton = itemView.findViewById(R.id.btnAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.nameTextView.text = product.name
        holder.priceTextView.text = "RS.${product.price}"
        Glide.with(holder.itemView.context)
            .load(product.imageUrl)
            .centerCrop()
            .into(holder.productImageView)

        holder.addToCartButton.setOnClickListener {
            Log.d("ProductAdapter", "Add to Cart clicked for: ${product.name}, Price: ${product.price}, ID: ${product.id}")
            val item = CartItem(0, product.name, product.price, 1)
            val isNewItem = cartService.addToCart(item)

            val message = if (isNewItem) {
                "${product.name} added to cart"
            } else {
                "${product.name} quantity increased in cart"
            }

            Toast.makeText(holder.itemView.context, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = products.size

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}