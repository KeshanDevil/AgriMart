package com.example.shopappp.services

import com.example.shopappp.models.AppUser
import com.example.shopappp.utils.Result
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserService {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("app_user   ")

    suspend fun createUser(user: AppUser): Result<Unit> {
        return try {
            usersCollection.document(user.id).set(user).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun getUser(userId: String): Result<AppUser?> {
        return try {
            val document = usersCollection.document(userId).get().await()
            val user = document.toObject(AppUser::class.java)
            Result.Success(user)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun updateUser(user: AppUser): Result<Unit> {
        return try {
            usersCollection.document(user.id).set(user).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun deleteUser(userId: String): Result<Unit> {
        return try {
            usersCollection.document(userId).delete().await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}