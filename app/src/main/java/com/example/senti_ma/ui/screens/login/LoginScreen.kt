package com.example.senti_ma.ui.screens.login

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.senti_ma.R
import com.example.senti_ma.ui.screens.login.components.SocialMediaButton
import com.example.senti_ma.ui.screens.login.events.LoginUiEvent
import com.example.senti_ma.ui.shared.AnnotatedTextBox
import com.example.senti_ma.ui.shared.IconImage
import com.example.senti_ma.ui.theme.black_incognito
import com.example.senti_ma.ui.theme.white_google

/**
 * Composable for the login screen.
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = hiltViewModel(),
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    var isUserPasswordVisible by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as? Activity

    LaunchedEffect(Unit) {
        activity?.let {
            loginViewModel.onEvent(LoginUiEvent.SignInWithSavedCredentials(it))
        }
    }

    Box(
        modifier = modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .clip(shapes.medium)
                .background(colorScheme.inverseSurface)
                .padding(25.dp)
        ) {
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = typography.headlineMedium,
                    color = colorScheme.inverseOnSurface,
                )

                Spacer(modifier = Modifier.width(10.dp))
                IconImage(
                    size = 40.dp,
                    modifier = Modifier
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                value = loginViewModel.userEmail,
                onValueChange = { loginViewModel.onEvent(LoginUiEvent.UpdateUserEmail(it)) },
                label = { Text(stringResource(R.string.text_field_user_email)) },
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
                value = loginViewModel.userPassword,
                onValueChange = { loginViewModel.onEvent(LoginUiEvent.UpdateUserPassword(it)) },
                visualTransformation = if (isUserPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                label = {
                    Text(stringResource(R.string.text_field_user_password))
                },
                trailingIcon = {
                    IconButton(onClick = { isUserPasswordVisible = !isUserPasswordVisible }) {
                        Icon(
                            imageVector = if (isUserPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = stringResource(R.string.description_icon_toggle_password_visibility)
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
                onClick = { loginViewModel.onEvent(LoginUiEvent.SignInWithEmailAndPassword) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.text_button_login))
            }

            Spacer(modifier = Modifier.height(16.dp))
            AnnotatedTextBox(
                textClickable = stringResource(R.string.text_message_clickable_forgot_password),
                onClick = {
                    loginViewModel.onEvent(LoginUiEvent.ClearState)
                    onForgotPasswordClick()
                },
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.separator_line),
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))
            SocialMediaButton(
                icon = R.drawable.ic_incognito,
                text = stringResource(R.string.text_button_sign_in_anonymously),
                colorText = Color.White,
                colorSurface = black_incognito,
                onClick = {
                    loginViewModel.onEvent(LoginUiEvent.SignInAnonymously)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            SocialMediaButton(
                icon = R.drawable.ic_google,
                text = stringResource(R.string.text_button_sign_in_with_google),
                colorText = Color.Black,
                colorSurface = white_google,
                onClick = {
                    activity?.let {
                        loginViewModel.onEvent(LoginUiEvent.SignInWithGoogle(it))
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnnotatedTextBox(
            text = stringResource(R.string.text_message_sign_up),
            textClickable = stringResource(R.string.text_message_clickable_sign_up),
            onClick = {
                loginViewModel.onEvent(LoginUiEvent.ClearState)
                onSignUpClick()
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        )
    }

}
