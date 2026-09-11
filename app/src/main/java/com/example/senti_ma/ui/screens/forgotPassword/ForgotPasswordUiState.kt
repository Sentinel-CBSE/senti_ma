package com.example.senti_ma.ui.screens.forgotPassword

/**
 * UI states for forgot password screen.
 */
sealed class ForgotPasswordUiState {
    data object Idle : ForgotPasswordUiState()
    data object Loading : ForgotPasswordUiState()
    data object Success : ForgotPasswordUiState()
    data class Error(val message: String) : ForgotPasswordUiState()
}
