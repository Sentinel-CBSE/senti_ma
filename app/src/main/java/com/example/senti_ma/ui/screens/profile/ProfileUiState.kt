package com.example.senti_ma.ui.screens.profile

sealed class ProfileUiState {
    data object Idle : ProfileUiState()
    data object Loading : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}
