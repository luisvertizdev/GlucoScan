package com.luisvertiz.nutriscan.features.login

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.luisvertiz.nutriscan.error.ErrorHandler.EmailNotVerifiedError
import com.luisvertiz.nutriscan.error.ErrorHandler.InvalidCredentialsError
import com.luisvertiz.nutriscan.error.ErrorHandler.InvalidUserError
import com.luisvertiz.nutriscan.error.ErrorHandler.UnknownError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepository @Inject constructor() {

    suspend fun login(
        email: String,
        password: String,
    ) = withContext(Dispatchers.IO) {
        try {
            val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
            val authResult: AuthResult = firebaseAuth.signInWithEmailAndPassword(
                email,
                password,
            ).await()

            val firebaseUser: FirebaseUser = authResult.user ?: throw UnknownError()
            firebaseUser.reload().await()

            if (firebaseUser.isEmailVerified.not()) {
                throw EmailNotVerifiedError()
            }
        } catch (_: FirebaseAuthInvalidUserException) {
            throw InvalidUserError()
        } catch (_: FirebaseAuthInvalidCredentialsException) {
            throw InvalidCredentialsError()
        } catch (_: Exception) {
            throw UnknownError()
        }
    }
}
