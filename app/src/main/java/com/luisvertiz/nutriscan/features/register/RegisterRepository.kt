package com.luisvertiz.nutriscan.features.register

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.luisvertiz.nutriscan.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegisterRepository @Inject constructor() {

    suspend fun register(fullName: String, email: String, password: String) {
        withContext(Dispatchers.IO) {
            try {
                val authResult: AuthResult = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
                val firebaseUser: FirebaseUser? = authResult.user

                if (firebaseUser == null) {
                    throw Exception("Ocurrió un error inesperado, inténtalo de nuevo.")
                }

                firebaseUser.sendEmailVerification().await()
                val uid: String = firebaseUser.uid
                val user = UserModel(
                    uid = uid,
                    fullName = fullName,
                    email = email
                )
                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(uid)
                    .set(user)
                    .await()
            } catch (exception: FirebaseAuthUserCollisionException) {
                throw Exception("El correo electrónico ya está registrado.")
            } catch (exception: Exception) {
                throw exception
            }
        }
    }
}
