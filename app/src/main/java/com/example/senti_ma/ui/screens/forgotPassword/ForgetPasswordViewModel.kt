package com.example.senti_ma.ui.screens.forgotPassword

import android.content.Context
import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.senti_ma.R
import com.example.senti_ma.domain.model.AppResult
import com.example.senti_ma.domain.usecase.AuthUseCases
import com.example.senti_ma.ui.screens.forgotPassword.events.ForgotPasswordUiEvent
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val firebaseAnalytics: FirebaseAnalytics,
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow<ForgotPasswordUiState>(ForgotPasswordUiState.Idle)
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    var userEmail by mutableStateOf("")
        private set

    fun onEvent(event: ForgotPasswordUiEvent) {
        when (event) {
            is ForgotPasswordUiEvent.ClearState -> clearState()
            is ForgotPasswordUiEvent.SendPasswordResetEmail -> sendPasswordResetEmail()
            is ForgotPasswordUiEvent.UpdateUserEmail -> updateUserEmail(event)
        }
    }

    private fun clearState() {
        _uiState.value = ForgotPasswordUiState.Idle
        userEmail = ""
    }

    private fun sendPasswordResetEmail() {
        if (userEmail.isBlank()) {
            _uiState.value = ForgotPasswordUiState.Error(context.getString(R.string.text_error_required_fields_are_null))
            return
        }

        _uiState.value = ForgotPasswordUiState.Loading
        viewModelScope.launch {
            when (val result = authUseCases.sendPasswordResetEmail(userEmail)) {
                is AppResult.Success -> {
                    _uiState.value = ForgotPasswordUiState.Success
                }
                is AppResult.Error -> {
                    _uiState.value = ForgotPasswordUiState.Error(result.errorMessage)
                    userEmail = ""
                }
            }
        }
    }

    private fun updateUserEmail(event: ForgotPasswordUiEvent.UpdateUserEmail) {
        _uiState.value = ForgotPasswordUiState.Idle
        userEmail = event.newUserEmail
    }

    fun logEvent(eventName: String, params: Bundle) {
        firebaseAnalytics.logEvent(eventName, params)
    }

}
