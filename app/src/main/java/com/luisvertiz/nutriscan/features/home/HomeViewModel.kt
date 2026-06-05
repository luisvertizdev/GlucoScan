package com.luisvertiz.nutriscan.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisvertiz.nutriscan.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val _user: MutableStateFlow<UserModel> = MutableStateFlow(UserModel())
    val user: StateFlow<UserModel> = _user

    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            try {
                val user: UserModel = homeRepository.getUser()
                _user.value = user
            } catch (exception: Exception) {
                _errorMessage.value = exception.message
            }
        }
    }

    fun dismissErrorDialog() {
        viewModelScope.launch {
            _errorMessage.value = null
        }
    }
}
