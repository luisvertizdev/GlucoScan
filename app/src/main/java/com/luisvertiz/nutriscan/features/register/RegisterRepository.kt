package com.luisvertiz.nutriscan.features.register

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.luisvertiz.nutriscan.error.ErrorHandler.EmailAlreadyRegisteredError
import com.luisvertiz.nutriscan.error.ErrorHandler.UnknownError
import com.luisvertiz.nutriscan.model.UserModel
import com.luisvertiz.nutriscan.util.FirestoreConstants.USERS_COLLECTION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegisterRepository @Inject constructor() {

    suspend fun register(
        fullName: String,
        email: String,
        password: String,
    ) = withContext(Dispatchers.IO) {
        try {
            val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
            val authResult: AuthResult = firebaseAuth.createUserWithEmailAndPassword(
                email,
                password,
            ).await()

            val firebaseUser: FirebaseUser = authResult.user ?: throw UnknownError()
            firebaseUser.sendEmailVerification().await()

            val uid: String = firebaseUser.uid
            val user = UserModel(
                uid = uid,
                fullName = fullName,
                email = email,
            )

            val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
            firebaseFirestore
                .collection(USERS_COLLECTION)
                .document(uid)
                .set(user)
                .await()
        } catch (_: FirebaseAuthUserCollisionException) {
            throw EmailAlreadyRegisteredError()
        } catch (_: Exception) {
            throw UnknownError()
        }
    }
}
