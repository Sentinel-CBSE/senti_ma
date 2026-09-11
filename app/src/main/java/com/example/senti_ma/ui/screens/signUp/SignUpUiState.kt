package com.example.senti_ma.ui.screens.signUp

/**
 * Sealed class representing the various states of the sign-up UI.
 */
sealed class SignUpUiState {
    data object Idle : SignUpUiState()
    data object Loading : SignUpUiState()
    data object Success : SignUpUiState()
    data class Error(val message: String) : SignUpUiState()
}
