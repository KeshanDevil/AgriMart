package com.example.shopappp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopappp.R
import com.example.shopappp.models.Order
import com.example.shopappp.services.OrderService

class OrderAdapter(private var orders: MutableList<Order>,private val orderService: OrderService) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {



    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderNameTextView: TextView = itemView.findViewById(R.id.orderNameTextView)
        val orderTotalTextView: TextView = itemView.findViewById(R.id.orderTotalTextView)
        val orderItemsTextView: TextView = itemView.findViewById(R.id.orderItemsTextView)
        val deleteOrderButton: Button = itemView.findViewById(R.id.deleteOrderButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)

        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.orderNameTextView.text = "Order by: ${order.name}"
        holder.orderTotalTextView.text = "Total: RS.${String.format("%.2f", order.totalAmount)}"
        holder.orderItemsTextView.text = "Items: ${order.items.joinToString { "${it.name} (x${it.quantity})" }}"

        holder.deleteOrderButton.setOnClickListener {
            deleteOrder(position,order.id)
        }
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    private fun deleteOrder(position: Int,id:Long) {
        orders.removeAt(position)
        orderService.deleteOrder(id)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, orders.size)
    }
}
