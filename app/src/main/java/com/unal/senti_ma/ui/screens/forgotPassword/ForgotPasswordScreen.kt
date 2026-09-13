package com.unal.senti_ma.ui.screens.forgotPassword

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.unal.senti_ma.R
import com.unal.senti_ma.ui.screens.forgotPassword.events.ForgotPasswordUiEvent
import com.unal.senti_ma.ui.shared.AnnotatedTextBox
import com.unal.senti_ma.ui.shared.AuthTopBar
import com.unal.senti_ma.ui.shared.IconImage

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    forgetPasswordViewModel: ForgetPasswordViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
    onSignUpClick: () -> Unit
) {
    val forgotPasswordUiState by forgetPasswordViewModel.uiState.collectAsState()

    val context = LocalContext.current
    val successMessage = stringResource(R.string.text_success_send_email)

    LaunchedEffect(forgotPasswordUiState) {
        when (val state = forgotPasswordUiState) {
            is ForgotPasswordUiState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is ForgotPasswordUiState.Success -> {
                Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Scaffold(topBar = {
        AuthTopBar(
            title = stringResource(R.string.title_forgot_password),
            onBackPressed = {
                forgetPasswordViewModel.onEvent(ForgotPasswordUiEvent.ClearState)
                onBackPressed()
            }
        )
    }) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .padding(20.dp)
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(innerPadding)
                    .clip(shapes.medium)
                    .background(colorScheme.surface)
                    .verticalScroll(rememberScrollState())
                    .padding(30.dp)
            ) {
                IconImage(
                    size = 120.dp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = stringResource(R.string.text_message_enter_email),
                    style = typography.bodyMedium,
                    color = colorScheme.onSurface,
                    modifier = Modifier
                )

                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = forgetPasswordViewModel.userEmail,
                    isError = forgotPasswordUiState is ForgotPasswordUiState.Error,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    onValueChange = { forgetPasswordViewModel.onEvent(ForgotPasswordUiEvent.UpdateUserEmail(it)) },
                    label = { Text(text = stringResource(R.string.text_field_user_email)) },
                    colors = TextFieldDefaults.colors(
                        cursorColor = colorScheme.primary,
                        focusedLabelColor = colorScheme.primaryContainer,
                        unfocusedLabelColor = colorScheme.onSurface,
                        focusedIndicatorColor = colorScheme.primary,
                        unfocusedIndicatorColor = colorScheme.onSurfaceVariant,
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { forgetPasswordViewModel.onEvent(ForgotPasswordUiEvent.SendPasswordResetEmail) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Text(text = stringResource(R.string.text_button_send_email))
                }

                if (forgotPasswordUiState is ForgotPasswordUiState.Loading) {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator(modifier = modifier.align(Alignment.CenterHorizontally))
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            AnnotatedTextBox(
                text = stringResource(R.string.text_message_sign_up),
                textClickable = stringResource(R.string.text_message_clickable_sign_up),
                onClick = {
                    forgetPasswordViewModel.onEvent(ForgotPasswordUiEvent.ClearState)
                    onSignUpClick()
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
            )
        }
    }
}
