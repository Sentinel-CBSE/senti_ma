package com.unal.senti_ma.ui.screens.signUp.components

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.unal.senti_ma.R
import com.unal.senti_ma.ui.screens.signUp.SignUpUiState
import com.unal.senti_ma.ui.screens.signUp.events.SignUpUiEvent
import com.unal.senti_ma.ui.shared.AuthTopBar
import com.unal.senti_ma.ui.shared.IconImage

@Composable
fun SignUpContent(
    modifier: Modifier = Modifier,
    uiState: SignUpUiState,
    userName: String,
    userEmail: String,
    userPassword: String,
    userPasswordConfirmation: String,
    onEvent: (SignUpUiEvent) -> Unit,
    onBackPressed: () -> Unit,
    onCreateUser: () -> Unit
) {
    var isUserPasswordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var isUserPasswordConfirmationVisible by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            AuthTopBar(
                title = stringResource(R.string.title_sign_up),
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
                    .align(Alignment.Center)
                    .clip(shapes.medium)
                    .background(colorScheme.surface)
                    .verticalScroll(rememberScrollState())
                    .padding(30.dp)
            ) {
                IconImage(
                    size = 100.dp,
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )
                )

                Spacer(
                    modifier = Modifier.height(70.dp)
                )

                TextField(
                    value = userName,
                    isError = uiState is SignUpUiState.Error,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    onValueChange = {
                        onEvent(
                            SignUpUiEvent.UpdateUserName(it)
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(
                                R.string.text_field_user_name
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

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                TextField(
                    value = userEmail,
                    isError = uiState is SignUpUiState.Error,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    onValueChange = {
                        onEvent(
                            SignUpUiEvent.UpdateUserEmail(it)
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

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                TextField(
                    value = userPassword,
                    isError = uiState is SignUpUiState.Error,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    onValueChange = {
                        onEvent(
                            SignUpUiEvent.UpdateUserPassword(it)
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
                            text = stringResource(
                                R.string.text_field_user_password
                            )
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                isUserPasswordVisible =
                                    !isUserPasswordVisible
                            }
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

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                TextField(
                    value = userPasswordConfirmation,
                    isError = uiState is SignUpUiState.Error,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    onValueChange = {
                        onEvent(
                            SignUpUiEvent.UpdateUserPasswordConfirmation(it)
                        )
                    },
                    visualTransformation =
                        if (isUserPasswordConfirmationVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                    label = {
                        Text(
                            text = stringResource(
                                R.string.text_field_user_password_confirmation
                            )
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                isUserPasswordConfirmationVisible =
                                    !isUserPasswordConfirmationVisible
                            }
                        ) {
                            Icon(
                                imageVector =
                                    if (isUserPasswordConfirmationVisible) {
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

                Spacer(
                    modifier = Modifier.height(32.dp)
                )

                Button(
                    onClick = onCreateUser,
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
                            R.string.text_button_sign_up
                        )
                    )
                }

                if (uiState is SignUpUiState.Loading) {
                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    CircularProgressIndicator(
                        modifier = Modifier.align(
                            Alignment.CenterHorizontally
                        )
                    )
                }
            }
        }
    }
}
