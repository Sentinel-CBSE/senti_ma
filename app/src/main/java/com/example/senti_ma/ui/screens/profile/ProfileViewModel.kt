package com.example.senti_ma.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.senti_ma.domain.usecase.AuthUseCases
import com.example.senti_ma.ui.screens.profile.events.ProfileUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.SignOut -> signOut()
        }
    }

    private fun signOut() {
        _uiState.value = ProfileUiState.Loading
        viewModelScope.launch {
            try {
                authUseCases.signOut()
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Error signing out")
            }
        }
    }

}
