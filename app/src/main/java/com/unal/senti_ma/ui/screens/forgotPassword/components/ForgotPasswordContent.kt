package com.unal.senti_ma.ui.screens.forgotPassword.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.unal.senti_ma.R
import com.unal.senti_ma.ui.screens.forgotPassword.ForgotPasswordUiState
import com.unal.senti_ma.ui.screens.forgotPassword.events.ForgotPasswordUiEvent
import com.unal.senti_ma.ui.shared.AnnotatedTextBox
import com.unal.senti_ma.ui.shared.AuthTopBar
import com.unal.senti_ma.ui.shared.IconImage

@Composable
fun ForgotPasswordContent(
    modifier: Modifier = Modifier,
    uiState: ForgotPasswordUiState,
    userEmail: String,
    onEvent: (ForgotPasswordUiEvent) -> Unit,
    onBackPressed: () -> Unit,
    onSignUpClick: () -> Unit
) {
    Scaffold(
        topBar = {
            AuthTopBar(
                title = stringResource(R.string.title_forgot_password),
                onBackPressed = onBackPressed
            )
        }
    ) { innerPadding ->
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
                    value = userEmail,
                    isError = uiState is ForgotPasswordUiState.Error,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    onValueChange = {
                        onEvent(
                            ForgotPasswordUiEvent.UpdateUserEmail(it)
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(
                                R.string.text_field_user_email
                            )
                        )
                    },
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
                    onClick = {
                        onEvent(
                            ForgotPasswordUiEvent.SendPasswordResetEmail
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Text(
                        text = stringResource(
                            R.string.text_button_send_email
                        )
                    )
                }

                if (uiState is ForgotPasswordUiState.Loading) {
                    Spacer(modifier = Modifier.height(16.dp))

                    CircularProgressIndicator(
                        modifier = Modifier.align(
                            Alignment.CenterHorizontally
                        )
                    )
                }
            }

            AnnotatedTextBox(
                text = stringResource(R.string.text_message_sign_up),
                textClickable = stringResource(
                    R.string.text_message_clickable_sign_up
                ),
                onClick = onSignUpClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
            )
        }
    }
}
