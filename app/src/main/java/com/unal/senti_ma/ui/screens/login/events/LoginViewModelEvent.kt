package com.unal.senti_ma.ui.screens.login.events

sealed interface LoginViewModelEvent {

    data object Success : LoginViewModelEvent

    data class Error(
        val message: String
    ) : LoginViewModelEvent

}
