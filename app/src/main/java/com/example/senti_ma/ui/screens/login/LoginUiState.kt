package com.example.senti_ma.ui.screens.login

/**
 * Represents the different states of the login UI.
 */
sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
    data object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
