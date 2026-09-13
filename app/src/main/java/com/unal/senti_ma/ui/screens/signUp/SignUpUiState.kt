package com.unal.senti_ma.ui.screens.signUp

sealed class SignUpUiState {
    data object Idle : SignUpUiState()
    data object Loading : SignUpUiState()
    data object Success : SignUpUiState()
    data class Error(val message: String) : SignUpUiState()
}
