package com.unal.senti_ma.ui.screens.forgotPassword.events

sealed interface ForgotPasswordUiEvent {
    data object ClearState: ForgotPasswordUiEvent
    data object SendPasswordResetEmail: ForgotPasswordUiEvent
    data class UpdateUserEmail(val newUserEmail: String): ForgotPasswordUiEvent
}
