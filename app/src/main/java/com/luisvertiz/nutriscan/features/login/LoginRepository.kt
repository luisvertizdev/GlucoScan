package com.luisvertiz.nutriscan.features.login

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginRepository @Inject constructor() {


    suspend fun login(email: String, password: String): AuthResult {
        try {
            val authResult: AuthResult = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
            return authResult
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw e
        } catch (e: FirebaseAuthInvalidUserException) {
            throw e
        }
    }
}
