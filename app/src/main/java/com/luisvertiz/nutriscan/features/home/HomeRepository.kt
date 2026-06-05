package com.luisvertiz.nutriscan.features.home

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.luisvertiz.nutriscan.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeRepository @Inject constructor() {

    suspend fun getUser(): UserModel {
        return withContext(Dispatchers.IO) {
            try {
                val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

                if (firebaseUser == null) {
                    throw Exception("Error al obtener los datos del usuario.")
                }

                val document = FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(firebaseUser.uid)
                    .get()
                    .await()

                val user = document.toObject(UserModel::class.java)

                if (user == null) {
                    throw Exception("Error al obtener los datos del usuario.")
                }

                return@withContext user
            } catch (exception: Exception) {
                throw exception
            }
        }
    }
}
