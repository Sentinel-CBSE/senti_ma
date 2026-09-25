package com.unal.senti_ma.ui.screens.forgotPassword

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.unal.senti_ma.ui.screens.forgotPassword.components.ForgotPasswordContent
import com.unal.senti_ma.ui.screens.forgotPassword.events.ForgotPasswordUiEvent
import com.unal.senti_ma.ui.screens.forgotPassword.events.ForgotPasswordViewModelEvent

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    forgetPasswordViewModel: ForgetPasswordViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
    onSignUpClick: () -> Unit
) {
    val forgotPasswordUiState by forgetPasswordViewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        forgetPasswordViewModel.viewModelEvent.collect { event ->
            when (event) {
                is ForgotPasswordViewModelEvent.Success -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is ForgotPasswordViewModelEvent.Error -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    ForgotPasswordContent(
        modifier = modifier,
        uiState = forgotPasswordUiState,
        userEmail = forgetPasswordViewModel.userEmail,
        onEvent = forgetPasswordViewModel::onEvent,
        onBackPressed = {
            forgetPasswordViewModel.onEvent(
                ForgotPasswordUiEvent.ClearState
            )
            onBackPressed()
        },
        onSignUpClick = {
            forgetPasswordViewModel.onEvent(
                ForgotPasswordUiEvent.ClearState
            )
            onSignUpClick()
        }
    )
}
