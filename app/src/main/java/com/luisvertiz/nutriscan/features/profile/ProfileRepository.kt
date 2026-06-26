package com.luisvertiz.nutriscan.features.profile

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.luisvertiz.nutriscan.error.ErrorHandler.UnknownError
import com.luisvertiz.nutriscan.model.UserModel
import com.luisvertiz.nutriscan.util.FirestoreConstants.USERS_COLLECTION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepository @Inject constructor() {

    suspend fun getUser(): UserModel = withContext(Dispatchers.IO) {
        try {
            val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


            val firebaseUser: FirebaseUser = firebaseAuth.currentUser ?: throw UnknownError()

            val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

            val document = firebaseFirestore
                .collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .get()
                .await()

            val user: UserModel = document.toObject(UserModel::class.java) ?: throw UnknownError()
            return@withContext user
        } catch (_: Exception) {
            throw UnknownError()
        }
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
    }
}