package com.example.shopappp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopappp.R
import com.example.shopappp.models.Equipment

class EquipmentAdapter(
    private val equipmentList: List<Equipment>,
    private val onAddToCartClick: (Equipment) -> Unit
) : RecyclerView.Adapter<EquipmentAdapter.EquipmentViewHolder>() {

    class EquipmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageViewEquipment)
        val nameTextView: TextView = view.findViewById(R.id.textViewName)
        val priceTextView: TextView = view.findViewById(R.id.textViewPrice)
        val addToCartButton: ImageButton = view.findViewById(R.id.buttonAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_equipment, parent, false)
        return EquipmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: EquipmentViewHolder, position: Int) {
        val equipment = equipmentList[position]
        holder.nameTextView.text = equipment.name
        holder.priceTextView.text = "RS.${equipment.price}"

        Glide.with(holder.imageView.context)
            .load(equipment.imageUrl)
                .placeholder(R.drawable.placeholder_image)
            .into(holder.imageView)

        holder.addToCartButton.setOnClickListener {
            onAddToCartClick(equipment)
        }
    }

    override fun getItemCount() = equipmentList.size
}