package com.example.shopappp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopappp.databinding.FragmentHomeBinding
import com.example.shopappp.models.Category
import com.example.shopappp.models.Deal
import com.example.shopappp.models.Product
import com.example.shopappp.services.ProductService
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var productService:ProductService = ProductService()

    private val categories = listOf(
        Category("Equipment", "https://cdn-icons-png.flaticon.com/512/10982/10982389.png"),
        Category("Fertilizer", "https://cdn-icons-png.flaticon.com/512/1670/1670075.png"),
        Category("VEGE", "https://cdn-icons-png.flaticon.com/512/10107/10107601.png"),
        Category("FRUITS", "https://cdn-icons-png.flaticon.com/512/1625/1625048.png")
    )

    private var products = listOf<Product>()

    private var deals = listOf<Deal>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCategoriesRecyclerView()
        setupHotDealsRecyclerView()
        updateHotDealsPromo()
        binding.tvSeeAll.setOnClickListener {
            onSeeAllClick()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            productService.getProductsStream().collectLatest { productList ->
                products = productList
                updateDeals()
                updateHotDealsPromo()
                (binding.rvHotDeals.adapter as HotDealsAdapter).updateDeals(deals)
            }
        }



    }

    private fun updateDeals() {
        deals = products.take(2).map { Deal(it, (10..50).random()) }
    }
    private suspend fun loadProducts(){
        try {
            // Fetch products from Firebase Firestore using ProductService


            // Update the UI with the fetched products and deals
            updateHotDealsPromo()
            setupHotDealsRecyclerView()
            setupCategoriesRecyclerView()  // Update this method if you also need categories from Firestore

        } catch (e: Exception) {
            // Handle any errors, like network issues
            e.printStackTrace()
        }
    }

    private fun setupCategoriesRecyclerView() {
        val adapter = CategoryAdapter(categories)
        binding.rvCategories.adapter = adapter
        binding.rvCategories.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupHotDealsRecyclerView() {
        val adapter = HotDealsAdapter(deals)
        binding.rvHotDeals.adapter = adapter
        binding.rvHotDeals.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun updateHotDealsPromo() {
        val maxDiscount = deals.maxOfOrNull { it.discountPercentage } ?: 0
        binding.tvHotDealsPromo.text = "HOT DEALS\n$maxDiscount% Off"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun onSeeAllClick() {
        // Navigate to SeeAllActivity
        val intent = Intent(requireContext(), AllProductsActivity::class.java)
        startActivity(intent)
    }


}

class CategoryAdapter(private val categories: List<Category>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconImageView: ImageView = itemView.findViewById(R.id.ivCategoryIcon)
        val nameTextView: TextView = itemView.findViewById(R.id.tvCategoryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.nameTextView.text = category.name
        Glide.with(holder.itemView.context)
            .load(category.iconUrl)
            .centerCrop()
            .into(holder.iconImageView)
    }

    override fun getItemCount() = categories.size
}


class HotDealsAdapter(private var deals: List<Deal>) :
    RecyclerView.Adapter<HotDealsAdapter.HotDealViewHolder>() {

    inner class HotDealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImageView: ImageView = itemView.findViewById(R.id.ivProductImage)
        val nameTextView: TextView = itemView.findViewById(R.id.tvProductName)
        val priceTextView: TextView = itemView.findViewById(R.id.tvProductPrice)
        val discountTextView: TextView = itemView.findViewById(R.id.tvDiscount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotDealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hot_deal, parent, false)
        return HotDealViewHolder(view)
    }

    override fun onBindViewHolder(holder: HotDealViewHolder, position: Int) {
        val deal = deals[position]
        holder.nameTextView.text = deal.product.name
        holder.priceTextView.text = "RS.${deal.product.price}"
        holder.discountTextView.text = "${deal.discountPercentage}% OFF"

        Glide.with(holder.itemView.context)
            .load(deal.product.imageUrl)
            .centerCrop()
            .into(holder.productImageView)
    }

    override fun getItemCount() = deals.size

    fun updateDeals(newDeals: List<Deal>) {
        deals = newDeals
        notifyDataSetChanged()
    }
}