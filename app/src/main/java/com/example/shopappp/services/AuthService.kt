    package com.example.shopappp.services

    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.auth.FirebaseUser
    import kotlinx.coroutines.tasks.await

    class AuthService {
        private val auth: FirebaseAuth = FirebaseAuth.getInstance()

        public suspend fun signInWithEmailAndPassword(email: String, password: String): Result<FirebaseUser> {
            return try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                Result.success(result.user!!)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

        public suspend fun signOut() {
            auth.signOut()
        }

        public suspend fun createUserWithEmailAndPassword(email: String, password: String): Result<FirebaseUser> {
            return try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                Result.success(result.user!!)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

        fun getCurrentUser(): FirebaseUser? {
            return auth.currentUser
        }
    }