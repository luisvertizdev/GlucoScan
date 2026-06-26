package com.luisvertiz.nutriscan.features.home

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.luisvertiz.nutriscan.error.ErrorHandler.UnknownError
import com.luisvertiz.nutriscan.model.MealModel
import com.luisvertiz.nutriscan.model.UserModel
import com.luisvertiz.nutriscan.util.FirestoreConstants.MEALS_COLLECTION
import com.luisvertiz.nutriscan.util.FirestoreConstants.USERS_COLLECTION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

class HomeRepository @Inject constructor() {

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

    suspend fun getTodayMeals(): List<MealModel> = withContext(Dispatchers.IO) {
        try {
            val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
            val firebaseUser: FirebaseUser = firebaseAuth.currentUser ?: throw UnknownError()
            val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

            val calendar = Calendar.getInstance()
            
            // Start of today
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val startOfDay = calendar.timeInMillis

            // End of today
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)
            val endOfDay = calendar.timeInMillis

            val querySnapshot = firebaseFirestore
                .collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .collection(MEALS_COLLECTION)
                .whereGreaterThanOrEqualTo("timestamp", startOfDay)
                .whereLessThanOrEqualTo("timestamp", endOfDay)
                .get()
                .await()

            return@withContext querySnapshot.toObjects(MealModel::class.java)
        } catch (_: Exception) {
            throw UnknownError()
        }
    }
}
