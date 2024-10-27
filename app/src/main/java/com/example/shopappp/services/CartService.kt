package com.example.shopappp.services

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.shopappp.models.CartItem
import com.example.shopappp.models.Equipment

class CartService(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "CartDatabase"
        private const val TABLE_CART = "cart"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_PRICE = "price"
        private const val KEY_QUANTITY = "quantity"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_CART("
                + "$KEY_ID INTEGER PRIMARY KEY,"
                + "$KEY_NAME TEXT,"
                + "$KEY_PRICE REAL,"
                + "$KEY_QUANTITY INTEGER)")
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        onCreate(db)
    }

    @SuppressLint("Range")
    fun addToCart(equipment: CartItem): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, equipment.name)
        values.put(KEY_PRICE, equipment.price)
        values.put(KEY_QUANTITY, 1) // Default quantity

        // Check if item already exists in cart
        val cursor = db.query(TABLE_CART, arrayOf(KEY_ID, KEY_QUANTITY), "$KEY_NAME=?",
            arrayOf(equipment.name), null, null, null)

        val isNewItem = !cursor.moveToFirst()

        if (!isNewItem) {
            // Item exists, update quantity
            val id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
            val quantity = cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY))
            values.put(KEY_QUANTITY, quantity + 1)
            db.update(TABLE_CART, values, "$KEY_ID=?", arrayOf(id.toString()))
        } else {
            // New item, insert
            db.insert(TABLE_CART, null, values)
        }
        cursor.close()
        db.close()
        return isNewItem
    }
    // Add these methods to your existing CartService class

    @SuppressLint("Range")
    fun getCartItems(): List<CartItem> {
        val cartItems = mutableListOf<CartItem>()
        val db = this.readableDatabase
        val cursor = db.query(TABLE_CART, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                val name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                val price = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE))
                val quantity = cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY))
                cartItems.add(CartItem(id, name, price, quantity))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return cartItems
    }

    fun updateQuantity(item: CartItem, newQuantity: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_QUANTITY, newQuantity)
        }
        db.update(TABLE_CART, values, "$KEY_ID = ?", arrayOf(item.id.toString()))
        db.close()
    }

    fun removeFromCart(item: CartItem) {
        val db = this.writableDatabase
        db.delete(TABLE_CART, "$KEY_ID = ?", arrayOf(item.id.toString()))
        db.close()
    }
}