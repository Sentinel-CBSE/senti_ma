package com.unal.senti_ma.ui.screens.signUp

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unal.senti_ma.R
import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.usecase.auth.CreateUserWithEmailAndPasswordUseCase
import com.unal.senti_ma.ui.screens.signUp.events.SignUpUiEvent
import com.unal.senti_ma.ui.screens.signUp.events.SignUpViewModelEvent
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
class SignUpViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val createUserWithEmailAndPasswordUseCase: CreateUserWithEmailAndPasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Idle)
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val _viewModelEvent = MutableSharedFlow<SignUpViewModelEvent>(replay = 0)
    val viewModelEvent = _viewModelEvent.asSharedFlow()

    var userName by mutableStateOf("")
        private set

    var userEmail by mutableStateOf("")
        private set

    var userPassword by mutableStateOf("")
        private set

    var userPasswordConfirmation by mutableStateOf("")
        private set

    fun onEvent(event: SignUpUiEvent) {
        when (event) {
            is SignUpUiEvent.ClearState -> clearState()
            is SignUpUiEvent.CreateUserWithEmailAndPassword -> createUserWithEmailAndPassword(event)
            is SignUpUiEvent.UpdateUserName -> updateUserName(event)
            is SignUpUiEvent.UpdateUserEmail -> updateUserEmail(event)
            is SignUpUiEvent.UpdateUserPassword -> updateUserPassword(event)
            is SignUpUiEvent.UpdateUserPasswordConfirmation -> updateUserPasswordConfirmation(event)
        }
    }

    private fun clearState() {
        _uiState.value = SignUpUiState.Idle

        userPasswordConfirmation = ""
        userPassword = ""
        userEmail = ""
        userName = ""
    }

    private fun createUserWithEmailAndPassword(
        event: SignUpUiEvent.CreateUserWithEmailAndPassword
    ) {
        if (
            userName.isBlank() ||
            userEmail.isBlank() ||
            userPassword.isBlank() ||
            userPasswordConfirmation.isBlank()
        ) {
            showError(
                context.getString(
                    R.string.text_error_required_fields_are_null
                )
            )
            return
        }

        if (userPassword != userPasswordConfirmation) {
            showError(
                context.getString(
                    R.string.text_error_passwords_fields_not_match
                )
            )
            return
        }

        if (userPassword.length < 8) {
            showError(
                context.getString(
                    R.string.text_error_invalid_password_length
                )
            )
            return
        }

        _uiState.value = SignUpUiState.Loading

        viewModelScope.launch {
            when (
                val result = createUserWithEmailAndPasswordUseCase(
                    userName,
                    userEmail,
                    userPassword,
                    event.activity
                )
            ) {
                is AppResult.Success -> {
                    _uiState.value = SignUpUiState.Idle

                    _viewModelEvent.emit(
                        SignUpViewModelEvent.Success
                    )
                }

                is AppResult.Error -> {
                    showError(result.errorMessage)

                    userPassword = ""
                    userEmail = ""
                }

                is AppResult.Cancelled -> {}
            }
        }
    }

    private fun updateUserName(
        event: SignUpUiEvent.UpdateUserName
    ) {
        _uiState.value = SignUpUiState.Idle
        userName = event.newUserName
    }

    private fun updateUserEmail(
        event: SignUpUiEvent.UpdateUserEmail
    ) {
        _uiState.value = SignUpUiState.Idle
        userEmail = event.newUserEmail
    }

    private fun updateUserPassword(
        event: SignUpUiEvent.UpdateUserPassword
    ) {
        _uiState.value = SignUpUiState.Idle
        userPassword = event.newUserPassword
    }

    private fun updateUserPasswordConfirmation(
        event: SignUpUiEvent.UpdateUserPasswordConfirmation
    ) {
        _uiState.value = SignUpUiState.Idle
        userPasswordConfirmation = event.newUserPasswordConfirmation
    }

    private fun showError(message: String) {
        _uiState.value = SignUpUiState.Error(message)

        viewModelScope.launch {
            _viewModelEvent.emit(
                SignUpViewModelEvent.Error(message)
            )
        }
    }

}
