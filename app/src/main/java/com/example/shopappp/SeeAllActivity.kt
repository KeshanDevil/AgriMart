package com.example.shopappp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopappp.EquipmentAdapter
import com.example.shopappp.models.Equipment
import com.example.shopappp.services.CartService

class SeeAllActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var equipmentAdapter: EquipmentAdapter
    private lateinit var cartService: CartService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_all)

        cartService = CartService(this)

        val category = intent.getStringExtra("category") ?: "All"
        title = "$category Equipment"

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val equipmentList = getFilteredEquipment(category)
        equipmentAdapter = EquipmentAdapter(equipmentList) { equipment ->
            //cartService.addToCart(equipment)
            // You can show a toast or snackbar here to confirm item added to cart
        }
        recyclerView.adapter = equipmentAdapter
    }

    private fun getFilteredEquipment(category: String): List<Equipment> {
        // This is a mock list. In a real app, you'd fetch this from a database or API
        val allEquipment = listOf(
            Equipment("Power Sprayers", 1700.00, "power_sprayer.jpg"),
            Equipment("Knap Sack Sprayers", 1800.00, "knap_sack_sprayer.jpg"),
            Equipment("Brush Cutters", 1202.50, "brush_cutter.jpg"),
            Equipment("Fogg Machines", 1500.00, "fogg_machine.jpg"),
            Equipment("Weeder", 1900.00, "weeder.jpg"),
            Equipment("Water Pump", 2400.00, "water_pump.jpg"),
            Equipment("Grain Collector", 180.00, "grain_collector.jpg")
        )
        return if (category == "All") allEquipment else allEquipment.filter { it.name.contains(category, ignoreCase = true) }
    }
}