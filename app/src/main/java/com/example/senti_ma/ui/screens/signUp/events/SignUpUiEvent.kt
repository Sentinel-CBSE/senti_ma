package com.example.senti_ma.ui.screens.signUp.events

import android.app.Activity

sealed interface SignUpUiEvent {
    data object ClearState: SignUpUiEvent
    data class CreateUserWithEmailAndPassword(val activity: Activity): SignUpUiEvent
    data class UpdateUserName(val newUserName: String): SignUpUiEvent
    data class UpdateUserEmail(val newUserEmail: String): SignUpUiEvent
    data class UpdateUserPassword(val newUserPassword: String): SignUpUiEvent
    data class UpdateUserPasswordConfirmation(val newUserPasswordConfirmation: String): SignUpUiEvent
}
