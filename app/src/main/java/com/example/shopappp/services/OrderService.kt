package com.example.shopappp.services

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.shopappp.models.Order
import com.example.shopappp.models.CartItem

class OrderService(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "shopapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_ORDERS = "orders"
        private const val TABLE_ORDER_ITEMS = "order_items"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createOrdersTable = """
            CREATE TABLE $TABLE_ORDERS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                address TEXT,
                contactNumber TEXT,
                email TEXT,
                totalAmount REAL
            )
        """.trimIndent()

        val createOrderItemsTable = """
            CREATE TABLE $TABLE_ORDER_ITEMS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                orderId INTEGER,
                name TEXT,
                price REAL,
                quantity INTEGER,
                FOREIGN KEY (orderId) REFERENCES $TABLE_ORDERS (id)
            )
        """.trimIndent()

        db?.execSQL(createOrdersTable)
        db?.execSQL(createOrderItemsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ORDERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ORDER_ITEMS")
        onCreate(db)
    }

    fun placeOrder(order: Order): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", order.name)
            put("address", order.address)
            put("contactNumber", order.contactNumber)
            put("email", order.email)
            put("totalAmount", order.totalAmount)
        }

        val orderId = db.insert(TABLE_ORDERS, null, values)

        // Insert ordered items
        order.items.forEach { item ->
            val itemValues = ContentValues().apply {
                put("orderId", orderId)
                put("name", item.name)
                put("price", item.price)
                put("quantity", item.quantity)
            }
            db.insert(TABLE_ORDER_ITEMS, null, itemValues)
        }

        return orderId
    }

    fun clearCart() {

    }


    fun getOrders(): List<Order> {
        val orders = mutableListOf<Order>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_ORDERS", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val address = cursor.getString(cursor.getColumnIndexOrThrow("address"))
                val contactNumber = cursor.getString(cursor.getColumnIndexOrThrow("contactNumber"))
                val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                val totalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow("totalAmount"))

                val items = getOrderItems(id) // Helper function to get order items for this order
                val order = Order(id, name, address, contactNumber, email, totalAmount, items)
                orders.add(order)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return orders
    }

    private fun getOrderItems(orderId: Long): List<CartItem> {
        val items = mutableListOf<CartItem>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_ORDER_ITEMS WHERE orderId = ?", arrayOf(orderId.toString()))

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
                val quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"))

                items.add(CartItem(0, name, price, quantity))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return items
    }

    fun deleteOrder(orderId: Long): Boolean {
        val db = writableDatabase
        db.beginTransaction() // Start a transaction to ensure both deletions happen atomically

        return try {
            // Delete associated items first
            db.delete(TABLE_ORDER_ITEMS, "orderId = ?", arrayOf(orderId.toString()))

            // Delete the order itself
            val rowsDeleted = db.delete(TABLE_ORDERS, "id = ?", arrayOf(orderId.toString()))

            db.setTransactionSuccessful() // Commit if both deletions succeed
            rowsDeleted > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false // Return false in case of failure
        } finally {
            db.endTransaction() // End the transaction (commit or rollback)
        }
    }


}
