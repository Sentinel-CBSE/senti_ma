package com.unal.senti_ma.ui.screens.login

import android.app.Activity
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.unal.senti_ma.ui.screens.login.components.LoginContent
import com.unal.senti_ma.ui.screens.login.events.LoginUiEvent
import com.unal.senti_ma.ui.screens.login.events.LoginViewModelEvent

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = hiltViewModel(),
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    val uiState by loginViewModel.uiState.collectAsState()
    val formState by loginViewModel.formState.collectAsState()

    val context = LocalContext.current
    val activity = context as? Activity

    var isUserPasswordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        loginViewModel.viewModelEvent.collect { event ->
            when (event) {
                is LoginViewModelEvent.Success -> {}

                is LoginViewModelEvent.Error -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        activity?.let {
            loginViewModel.onEvent(
                LoginUiEvent.SignInWithSavedCredentials(it)
            )
        }
    }

    LoginContent(
        modifier = modifier,
        uiState = uiState,
        formState = formState,
        isUserPasswordVisible = isUserPasswordVisible,
        onPasswordVisibilityChange = {
            isUserPasswordVisible = !isUserPasswordVisible
        },
        onEvent = loginViewModel::onEvent,
        onForgotPasswordClick = {
            loginViewModel.onEvent(
                LoginUiEvent.ClearState
            )
            onForgotPasswordClick()
        },
        onSignUpClick = {
            loginViewModel.onEvent(
                LoginUiEvent.ClearState
            )
            onSignUpClick()
        },
        onGoogleSignInClick = {
            activity?.let {
                loginViewModel.onEvent(
                    LoginUiEvent.SignInWithGoogle(it)
                )
            }
        }
    )
}
