package com.example.shopappp.services


import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import com.example.shopappp.models.Deal
import com.example.shopappp.models.Product
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
class ProductService {

    private val firestore = FirebaseFirestore.getInstance()
    private val inventoryCollection = firestore.collection("inventory")
    private val productsCollection = firestore.collection("inventory")
    // Function to get all products from the "inventory" collection
    suspend fun getProducts(): List<Product> {
        return try {
            val snapshot = inventoryCollection.get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject<Product>()  // Converts Firestore document to Product object
            }
        } catch (e: Exception) {
            // Handle any exceptions (e.g., network issues)
            emptyList()
        }
    }

    // If you have a specific deal fetching function
    suspend fun getDeals(): List<Deal> {
        return try {
            val snapshot = inventoryCollection.get().await()
            snapshot.documents.mapNotNull { doc ->
                val product = doc.toObject<Product>()
                // Assuming each product document has a 'discountPercentage' field for deals
                val discountPercentage = doc.getLong("discountPercentage")?.toInt()
                if (product != null && discountPercentage != null) {
                    Deal(product, discountPercentage)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            emptyList()  // Return an empty list if something goes wrong
        }
    }
    fun getProductsStream(): Flow<List<Product>> = callbackFlow {
        val listener = productsCollection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val products = snapshot.toObjects(Product::class.java)
                trySend(products)
            }
        }

        awaitClose { listener.remove() }
    }
}
