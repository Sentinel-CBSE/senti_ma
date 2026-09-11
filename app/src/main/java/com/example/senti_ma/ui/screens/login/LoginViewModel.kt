package com.example.senti_ma.ui.screens.login

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.senti_ma.R
import com.example.senti_ma.domain.model.AuthResult
import com.example.senti_ma.domain.usecase.AuthUseCases
import com.example.senti_ma.ui.screens.login.events.LoginUiEvent
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for handling login logic and UI state.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firebaseAnalytics: FirebaseAnalytics,
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()

    var userEmail by mutableStateOf("")
        private set

    var userPassword by mutableStateOf("")
        private set

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.ClearState -> clearState()
            is LoginUiEvent.SignInAnonymously -> onSignInAnonymously()
            is LoginUiEvent.SignInWithEmailAndPassword -> onSignInWithEmailAndPassword()
            is LoginUiEvent.SignInWithSavedCredentials -> onSignInWithSavedCredentials(event.activity)
            is LoginUiEvent.SignInWithGoogle -> onSignInWithGoogle(event.activity)
            is LoginUiEvent.UpdateUserEmail -> updateUserEmail(event.newUserEmail)
            is LoginUiEvent.UpdateUserPassword -> updateUserPassword(event.newUserPassword)
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
                is AuthResult.Success -> _uiState.value = LoginUiState.Success
                is AuthResult.Error -> _uiState.value = LoginUiState.Error(result.errorMessage)
            }
        }
    }

    private fun onSignInWithSavedCredentials(activity: Activity) {
        viewModelScope.launch {
            when (val result = authUseCases.signInWithSavedCredentials(activity)) {
                is AuthResult.Success -> _uiState.value = LoginUiState.Success
                is AuthResult.Error -> {
                    if (result.errorMessage != "activity is cancelled by the user.") {
                        _uiState.value = LoginUiState.Error(result.errorMessage)
                    } else {
                        _uiState.value = LoginUiState.Idle
                    }
                }
            }
        }
    }

    private fun onSignInWithGoogle(activity: Activity) {
        _uiState.value = LoginUiState.Loading
        viewModelScope.launch {
            when (val result = authUseCases.signInWithGoogle(activity)) {
                is AuthResult.Success -> _uiState.value = LoginUiState.Success
                is AuthResult.Error -> {
                    if (result.errorMessage != "activity is cancelled by the user.") {
                        _uiState.value = LoginUiState.Error(result.errorMessage)
                    } else {
                        _uiState.value = LoginUiState.Idle
                    }
                }
            }
        }
    }

    private fun onSignInWithEmailAndPassword() {
        _uiState.value = LoginUiState.Loading

        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            _uiState.value = LoginUiState.Error(context.getString(R.string.text_error_required_fields_are_null))
            return
        }

        viewModelScope.launch {
            when (val result = authUseCases.signInWithEmailAndPassword(userEmail, userPassword)) {
                is AuthResult.Success -> _uiState.value = LoginUiState.Success
                is AuthResult.Error -> {
                    _uiState.value = LoginUiState.Error(result.errorMessage)
                    userPassword = ""
                    userEmail = ""
                }
            }
        }
    }

    private fun updateUserEmail(newUserEmail: String) {
        _uiState.value = LoginUiState.Idle
        userEmail = newUserEmail
    }

    private fun updateUserPassword(newUserPassword: String) {
        _uiState.value = LoginUiState.Idle
        userPassword = newUserPassword
    }

    fun logEvent(eventName: String, params: Bundle) {
        firebaseAnalytics.logEvent(eventName, params)
    }
}
