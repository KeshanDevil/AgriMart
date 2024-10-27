package com.example.shopappp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopappp.databinding.ItemCartBinding
import com.example.shopappp.models.CartItem

class CartAdapter(
    private val onQuantityChanged: (CartItem, Int) -> Unit,
    private val onItemRemoved: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var cartItems: List<CartItem> = emptyList()

    inner class CartViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItem) {
            binding.productNameTextView.text = item.name
            binding.productPriceTextView.text = "RS. ${item.price}"
            binding.quantityTextView.text = item.quantity.toString()

            binding.addButton.setOnClickListener {
                val newQuantity = item.quantity + 1
                onQuantityChanged(item, newQuantity)
            }

            binding.minusButton.setOnClickListener {
                if (item.quantity > 1) { // Ensure quantity doesn't go below 1
                    val newQuantity = item.quantity - 1
                    onQuantityChanged(item, newQuantity)
                }
            }

            binding.removeButton.setOnClickListener {
                onItemRemoved(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount(): Int = cartItems.size

    fun submitList(items: List<CartItem>) {
        cartItems = items
        notifyDataSetChanged()
    }
}
