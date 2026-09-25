package com.unal.senti_ma.ui.screens.login.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.unal.senti_ma.R
import com.unal.senti_ma.ui.screens.login.LoginFormState
import com.unal.senti_ma.ui.screens.login.LoginUiState
import com.unal.senti_ma.ui.screens.login.events.LoginUiEvent
import com.unal.senti_ma.ui.shared.AnnotatedTextBox
import com.unal.senti_ma.ui.shared.IconImage
import com.unal.senti_ma.ui.theme.white_google

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    formState: LoginFormState,
    isUserPasswordVisible: Boolean,
    onPasswordVisibilityChange: () -> Unit,
    onEvent: (LoginUiEvent) -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onGoogleSignInClick: () -> Unit
) {
    if (uiState is LoginUiState.Loading) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        return
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .clip(shapes.medium)
                .background(colorScheme.surface)
                .padding(25.dp)
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = typography.headlineMedium,
                color = colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            IconImage(
                size = 72.dp,
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = formState.userEmail,
                isError = uiState is LoginUiState.Error,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                onValueChange = {
                    onEvent(
                        LoginUiEvent.UpdateUserEmail(it)
                    )
                },
                label = {
                    Text(
                        stringResource(
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

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = formState.userPassword,
                isError = uiState is LoginUiState.Error,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                onValueChange = {
                    onEvent(
                        LoginUiEvent.UpdateUserPassword(it)
                    )
                },
                visualTransformation =
                    if (isUserPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                label = {
                    Text(
                        stringResource(
                            R.string.text_field_user_password
                        )
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = onPasswordVisibilityChange
                    ) {
                        Icon(
                            imageVector =
                                if (isUserPasswordVisible) {
                                    Icons.Default.Visibility
                                } else {
                                    Icons.Default.VisibilityOff
                                },
                            contentDescription = stringResource(
                                R.string.description_icon_toggle_password_visibility
                            )
                        )
                    }
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

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onEvent(
                        LoginUiEvent.SignInWithEmailAndPassword
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(
                        R.string.text_button_login
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnnotatedTextBox(
                textClickable = stringResource(
                    R.string.text_message_clickable_forgot_password
                ),
                onClick = onForgotPasswordClick,
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.separator_line),
                color = colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            SocialMediaButton(
                icon = R.drawable.ic_google,
                text = stringResource(
                    R.string.text_button_sign_in_with_google
                ),
                colorText = Color.Black,
                colorSurface = white_google,
                onClick = onGoogleSignInClick,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AnnotatedTextBox(
                text = stringResource(
                    R.string.text_message_sign_up
                ),
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
