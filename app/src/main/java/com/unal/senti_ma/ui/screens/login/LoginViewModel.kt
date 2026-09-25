package com.unal.senti_ma.ui.screens.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unal.senti_ma.R
import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.usecase.auth.SignInAnonymouslyUseCase
import com.unal.senti_ma.domain.usecase.auth.SignInWithEmailAndPasswordUseCase
import com.unal.senti_ma.domain.usecase.auth.SignInWithGoogleUseCase
import com.unal.senti_ma.domain.usecase.auth.SignInWithSavedCredentialsUseCase
import com.unal.senti_ma.ui.screens.login.events.LoginUiEvent
import com.unal.senti_ma.ui.screens.login.events.LoginViewModelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val signInWithSavedCredentialsUseCase: SignInWithSavedCredentialsUseCase,
    private val signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase,
    private val signInAnonymouslyUseCase: SignInAnonymouslyUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(LoginFormState())
    val formState: StateFlow<LoginFormState> = _formState.asStateFlow()

    private val _viewModelEvent = MutableSharedFlow<LoginViewModelEvent>(replay = 0)
    val viewModelEvent = _viewModelEvent.asSharedFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.ClearState -> clearState()
            is LoginUiEvent.SignInAnonymously -> onSignInAnonymously()
            is LoginUiEvent.SignInWithEmailAndPassword -> onSignInWithEmailAndPassword()
            is LoginUiEvent.SignInWithSavedCredentials -> onSignInWithSavedCredentials(event)
            is LoginUiEvent.SignInWithGoogle -> onSignInWithGoogle(event)
            is LoginUiEvent.UpdateUserEmail -> updateUserEmail(event)
            is LoginUiEvent.UpdateUserPassword -> updateUserPassword(event)
        }
    }

    private fun clearState() {
        _uiState.value = LoginUiState.Idle
        _formState.value = LoginFormState()
    }

    private fun onSignInAnonymously() {
        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            when (val result = signInAnonymouslyUseCase()) {
                is AppResult.Success -> {
                    _uiState.value = LoginUiState.Idle
                    _viewModelEvent.emit(
                        LoginViewModelEvent.Success
                    )
                }

                is AppResult.Error -> showError(result.errorMessage)

                is AppResult.Cancelled -> Unit
            }
        }
    }

    private fun onSignInWithSavedCredentials(
        event: LoginUiEvent.SignInWithSavedCredentials
    ) {
        viewModelScope.launch {
            when (
                val result = signInWithSavedCredentialsUseCase(
                    event.activity
                )
            ) {
                is AppResult.Success -> {
                    _uiState.value = LoginUiState.Idle
                    _viewModelEvent.emit(
                        LoginViewModelEvent.Success
                    )
                }

                is AppResult.Error -> showError(result.errorMessage)

                is AppResult.Cancelled -> Unit
            }
        }
    }

    private fun onSignInWithGoogle(
        event: LoginUiEvent.SignInWithGoogle
    ) {
        viewModelScope.launch {
            when (
                val result = signInWithGoogleUseCase(
                    event.activity
                )
            ) {
                is AppResult.Success -> {
                    _uiState.value = LoginUiState.Idle
                    _viewModelEvent.emit(
                        LoginViewModelEvent.Success
                    )
                }

                is AppResult.Error -> showError(result.errorMessage)

                is AppResult.Cancelled -> Unit
            }
        }
    }

    private fun onSignInWithEmailAndPassword() {
        val formState = _formState.value

        if (formState.userEmail.isBlank() ||
            formState.userPassword.isBlank()
        ) {
            showError(context.getString(R.string.text_error_required_fields_are_null))
            return
        }

        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            when (
                val result = signInWithEmailAndPasswordUseCase(
                    formState.userEmail,
                    formState.userPassword
                )
            ) {
                is AppResult.Success -> {
                    _uiState.value = LoginUiState.Idle
                    _viewModelEvent.emit(
                        LoginViewModelEvent.Success
                    )
                }

                is AppResult.Error -> {
                    showError(result.errorMessage)
                    _formState.value = LoginFormState()
                }

                is AppResult.Cancelled -> Unit
            }
        }
    }

    private fun updateUserEmail(event: LoginUiEvent.UpdateUserEmail) {
        _uiState.value = LoginUiState.Idle

        _formState.update {
            it.copy(
                userEmail = event.newUserEmail
            )
        }
    }

    private fun updateUserPassword(event: LoginUiEvent.UpdateUserPassword) {
        _uiState.value = LoginUiState.Idle

        _formState.update {
            it.copy(
                userPassword = event.newUserPassword
            )
        }
    }

    private fun showError(message: String) {
        _uiState.value = LoginUiState.Error(message)

        viewModelScope.launch {
            _viewModelEvent.emit(
                LoginViewModelEvent.Error(message)
            )
        }
    }

}
