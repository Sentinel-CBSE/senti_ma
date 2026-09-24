package com.unal.senti_ma.ui.screens.forgotPassword

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unal.senti_ma.R
import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.usecase.auth.SendPasswordResetEmailUseCase
import com.unal.senti_ma.ui.screens.forgotPassword.events.ForgotPasswordUiEvent
import com.unal.senti_ma.ui.screens.forgotPassword.events.ForgotPasswordViewModelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val sendPasswordResetEmailUseCase: SendPasswordResetEmailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ForgotPasswordUiState>(ForgotPasswordUiState.Idle)
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    private val _viewModelEvent = MutableSharedFlow<ForgotPasswordViewModelEvent>(replay = 0)
    val viewModelEvent = _viewModelEvent.asSharedFlow()

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
            showError(context.getString(R.string.text_error_required_fields_are_null))
            return
        }

        _uiState.value = ForgotPasswordUiState.Loading
        viewModelScope.launch {
            when (val result = sendPasswordResetEmailUseCase(userEmail)) {
                is AppResult.Success -> {
                    _uiState.value = ForgotPasswordUiState.Idle
                    _viewModelEvent.emit(
                        ForgotPasswordViewModelEvent.Success(
                            context.getString(R.string.text_success_send_email)
                        )
                    )
                }

                is AppResult.Error -> showError(result.errorMessage)
                is AppResult.Cancelled -> {}
            }
        }
    }

    private fun updateUserEmail(event: ForgotPasswordUiEvent.UpdateUserEmail) {
        _uiState.value = ForgotPasswordUiState.Idle
        userEmail = event.newUserEmail
    }

    private fun showError(message: String) {
        _uiState.value = ForgotPasswordUiState.Error(message)
        viewModelScope.launch { _viewModelEvent.emit(ForgotPasswordViewModelEvent.Error(message)) }
    }

}
