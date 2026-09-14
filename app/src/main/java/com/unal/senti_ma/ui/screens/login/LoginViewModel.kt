package com.unal.senti_ma.ui.screens.login

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unal.senti_ma.R
import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.usecase.AuthUseCases
import com.unal.senti_ma.ui.screens.login.events.LoginUiEvent
import com.unal.senti_ma.ui.screens.login.events.LoginViewModelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _viewModelEvent = MutableSharedFlow<LoginViewModelEvent>(replay = 0)
    val viewModelEvent = _viewModelEvent.asSharedFlow()

    var userEmail by mutableStateOf("")
        private set

    var userPassword by mutableStateOf("")
        private set

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
        userPassword = ""
        userEmail = ""
    }

    private fun onSignInAnonymously() {
        _uiState.value = LoginUiState.Loading
        viewModelScope.launch {
            when (val result = authUseCases.signInAnonymously()) {
                is AppResult.Success -> {
                    _uiState.value = LoginUiState.Idle
                    _viewModelEvent.emit(LoginViewModelEvent.Success)
                }

                is AppResult.Error -> showError(result.errorMessage)
                is AppResult.Cancelled -> {}
            }
        }
    }

    private fun onSignInWithSavedCredentials(event: LoginUiEvent.SignInWithSavedCredentials) {
        viewModelScope.launch {
            when (val result = authUseCases.signInWithSavedCredentials(event.activity)) {
                is AppResult.Success -> {
                    _uiState.value = LoginUiState.Idle
                    _viewModelEvent.emit(LoginViewModelEvent.Success)
                }

                is AppResult.Error -> showError(result.errorMessage)
                is AppResult.Cancelled -> {}
            }
        }
    }

    private fun onSignInWithGoogle(event: LoginUiEvent.SignInWithGoogle) {
        viewModelScope.launch {
            when (val result = authUseCases.signInWithGoogle(event.activity)) {
                is AppResult.Success -> {
                    _uiState.value = LoginUiState.Idle
                    _viewModelEvent.emit(LoginViewModelEvent.Success)
                }

                is AppResult.Error -> showError(result.errorMessage)
                is AppResult.Cancelled -> {}
            }
        }
    }

    private fun onSignInWithEmailAndPassword() {
        if (userEmail.isBlank() || userPassword.isBlank()) {
            showError(context.getString(R.string.text_error_required_fields_are_null))
            return
        }

        _uiState.value = LoginUiState.Loading
        viewModelScope.launch {
            when (val result = authUseCases.signInWithEmailAndPassword(userEmail, userPassword)) {
                is AppResult.Success -> {
                    _uiState.value = LoginUiState.Idle
                    _viewModelEvent.emit(LoginViewModelEvent.Success)
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

    private fun updateUserEmail(event: LoginUiEvent.UpdateUserEmail) {
        _uiState.value = LoginUiState.Idle
        userEmail = event.newUserEmail
    }

    private fun updateUserPassword(event: LoginUiEvent.UpdateUserPassword) {
        _uiState.value = LoginUiState.Idle
        userPassword = event.newUserPassword
    }

    private fun showError(message: String) {
        _uiState.value = LoginUiState.Error(message)
        viewModelScope.launch { _viewModelEvent.emit(LoginViewModelEvent.Error(message)) }
    }

}
