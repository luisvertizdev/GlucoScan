package com.luisvertiz.nutriscan.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.luisvertiz.nutriscan.navigation.main.MainNavigationRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _startDestination: MutableStateFlow<MainNavigationRoute> = MutableStateFlow(MainNavigationRoute.Login)
    val startDestination: StateFlow<MainNavigationRoute> = _startDestination

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

                if (firebaseUser != null) {
                    _startDestination.value = MainNavigationRoute.Dashboard
                } else {
                    _startDestination.value = MainNavigationRoute.Login
                }
            } catch (_: Exception) {
                _startDestination.value = MainNavigationRoute.Login
            } finally {
                _isLoading.value = false
            }
        }
    }
}