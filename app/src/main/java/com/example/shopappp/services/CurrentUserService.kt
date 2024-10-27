package com.example.shopappp.services

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.shopappp.models.AppUser

class CurrentUserService(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "shopApp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USERS = "users"
        private const val COLUMN_USER_ID = "id"
        private const val COLUMN_USER_NAME = "name"
        private const val COLUMN_USER_ADDRESS = "address"
        private const val COLUMN_USER_EMAIL = "email"
        private const val COLUMN_USER_PHONE = "phoneNumber"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID TEXT PRIMARY KEY,
                $COLUMN_USER_NAME TEXT,
                $COLUMN_USER_ADDRESS TEXT,
                $COLUMN_USER_EMAIL TEXT,
                $COLUMN_USER_PHONE TEXT
            )
        """.trimIndent()
        db.execSQL(createUsersTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun getUser(userId: String): AppUser? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            null,
            "$COLUMN_USER_ID = ?",
            arrayOf(userId),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val user = AppUser(
                id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)),
                address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ADDRESS)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)),
                phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PHONE))
            )
            cursor.close()
            user
        } else {
            cursor.close()
            null
        }
    }

    fun updateUser(user: AppUser): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_USER_ID, user.id)
            put(COLUMN_USER_NAME, user.name)
            put(COLUMN_USER_ADDRESS, user.address)
            put(COLUMN_USER_EMAIL, user.email)
            put(COLUMN_USER_PHONE, user.phoneNumber)
        }

        val result = db.update(TABLE_USERS, contentValues, "$COLUMN_USER_ID = ?", arrayOf(user.id))
        return result > 0
    }

    fun createUser(user: AppUser): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_USER_ID, user.id)
            put(COLUMN_USER_NAME, user.name)
            put(COLUMN_USER_ADDRESS, user.address)
            put(COLUMN_USER_EMAIL, user.email)
            put(COLUMN_USER_PHONE, user.phoneNumber)
        }

        val result = db.insert(TABLE_USERS, null, contentValues)
        return result != -1L
    }

    fun deleteUser(userId: String): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_USERS, "$COLUMN_USER_ID = ?", arrayOf(userId))
        return result > 0
    }
}
