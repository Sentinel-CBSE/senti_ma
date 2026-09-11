package com.example.senti_ma.ui.screens.forgotPassword

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.senti_ma.R
import com.example.senti_ma.ui.screens.forgotPassword.events.ForgotPasswordUiEvent
import com.example.senti_ma.ui.shared.AuthTopBar
import com.example.senti_ma.ui.shared.AnnotatedTextBox
import com.example.senti_ma.ui.shared.IconImage

/**
 * Forgot password screen.
 */
@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    forgetPasswordViewModel: ForgetPasswordViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
    onSignUpClick: () -> Unit
) {
    val uiState by forgetPasswordViewModel.uiState.collectAsState()
    val context = LocalContext.current

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
                    .background(colorScheme.inverseSurface)
                    .verticalScroll(rememberScrollState())
                    .padding(30.dp)
            ) {
                IconImage(
                    size = 120.dp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.text_message_enter_email),
                    style = typography.bodyMedium,
                    color = colorScheme.inverseOnSurface,
                    modifier = Modifier
                )

                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = forgetPasswordViewModel.userEmail,
                    singleLine = true,
                    shape = shapes.large,
                    isError = uiState is ForgotPasswordUiState.Error,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    label = {
                        Text(text = stringResource(R.string.text_field_user_email))
                    },
                    onValueChange = {
                        forgetPasswordViewModel.onEvent(
                            ForgotPasswordUiEvent.UpdateUserEmail(it)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorScheme.primary,
                        focusedTextColor = colorScheme.inverseOnSurface,
                        focusedLabelColor = colorScheme.inverseOnSurface,
                        unfocusedLabelColor = colorScheme.inverseOnSurface,
                        unfocusedBorderColor = colorScheme.inverseOnSurface.copy(alpha = 0.5f),
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        forgetPasswordViewModel.onEvent(ForgotPasswordUiEvent.SendPasswordResetEmail)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    ),
                    shape = shapes.large,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .shadow(30.dp, shapes.large)
                ) {
                    Text(text = stringResource(R.string.text_button_send_email))
                }

                when (val state = uiState) {
                    is ForgotPasswordUiState.Loading -> {
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator(
                            modifier = modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                    is ForgotPasswordUiState.Error -> {
                        Toast.makeText(
                            context,
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is ForgotPasswordUiState.Success -> {
                        Toast.makeText(
                            context,
                            stringResource(R.string.text_success_send_email),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {}
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
