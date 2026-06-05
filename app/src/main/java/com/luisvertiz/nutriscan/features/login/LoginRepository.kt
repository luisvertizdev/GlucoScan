package com.luisvertiz.nutriscan.features.login

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepository @Inject constructor() {

    suspend fun login(email: String, password: String) {
        withContext(Dispatchers.IO) {
            try {
                val authResult: AuthResult = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
                val firebaseUser: FirebaseUser? = authResult.user
                firebaseUser?.reload()?.await()

                if (firebaseUser == null) {
                    throw Exception("Ocurrió un error inesperado, inténtalo de nuevo.")
                }

                if (firebaseUser.isEmailVerified.not()) {
                    throw Exception("Verifica tu correo electrónico.")
                }
            } catch (exception: FirebaseAuthInvalidUserException) {
                throw Exception("El correo electrónico no está registrado.")
            } catch (exception: FirebaseAuthInvalidCredentialsException) {
                throw Exception("La contraseña es incorrecta.")
            } catch (exception: Exception) {
                throw exception
            }
        }
    }
}
