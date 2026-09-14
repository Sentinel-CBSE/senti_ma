package com.unal.senti_ma.ui.screens.forgotPassword.events

sealed interface ForgotPasswordViewModelEvent {

    data class Success(
        val message: String
    ) : ForgotPasswordViewModelEvent

    data class Error(
        val message: String
    ) : ForgotPasswordViewModelEvent

}
