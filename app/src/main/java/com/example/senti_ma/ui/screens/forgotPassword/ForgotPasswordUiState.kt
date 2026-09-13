package com.example.senti_ma.ui.screens.forgotPassword

sealed class ForgotPasswordUiState {
    data object Idle : ForgotPasswordUiState()
    data object Loading : ForgotPasswordUiState()
    data object Success : ForgotPasswordUiState()
    data class Error(val message: String) : ForgotPasswordUiState()
}
