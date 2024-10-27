package com.example.shopappp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopappp.OrderAdapter
import com.example.shopappp.databinding.FragmentOrderBinding
import com.example.shopappp.services.OrderService

class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private lateinit var orderService: OrderService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderService = OrderService(requireContext())
        setupRecyclerView()
        loadOrders()
    }

    private fun setupRecyclerView() {
        binding.ordersRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun loadOrders() {
        val orders = orderService.getOrders().toMutableList()
        binding.ordersRecyclerView.adapter = OrderAdapter(orders,orderService)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
