package com.example.senti_ma.ui.screens.forgotPassword.events

/**
 * UI events for Forgot Password screen.
 */
sealed interface ForgotPasswordUiEvent {

    data object ClearState: ForgotPasswordUiEvent

    data object SendPasswordResetEmail: ForgotPasswordUiEvent

    data class UpdateUserEmail(val newUserEmail: String): ForgotPasswordUiEvent

}
