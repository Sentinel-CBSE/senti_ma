package com.example.senti_ma.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.senti_ma.domain.usecase.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    fun loadUserData() {
        val currentUser = authUseCases.getCurrentUser()
        _uiState.update {
            it.copy(user = currentUser, isLoading = false)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authUseCases.signOut()
            _uiState.update { it.copy(user = null) }
        }
    }
}
