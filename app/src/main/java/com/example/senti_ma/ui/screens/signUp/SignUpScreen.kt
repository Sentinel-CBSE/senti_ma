package com.example.senti_ma.ui.screens.signUp

import android.app.Activity
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.senti_ma.R
import com.example.senti_ma.ui.screens.signUp.events.SignUpUiEvent
import com.example.senti_ma.ui.shared.AuthTopBar
import com.example.senti_ma.ui.shared.IconImage

/**
 * Composable for the sign-up screen, handling user input and account creation.
 */
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    handleLoginNavigation: () -> Unit
) {
    val uiState by signUpViewModel.uiState.collectAsState()
    var isUserPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isUserPasswordConfirmationVisible by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as? Activity

    Scaffold(topBar = {
        AuthTopBar(
            title = stringResource(R.string.title_sign_up),
            onBackPressed = {
                signUpViewModel.onEvent(SignUpUiEvent.ClearState)
                handleLoginNavigation()
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
                    .align(Alignment.Center)
                    .clip(shapes.medium)
                    .background(colorScheme.inverseSurface)
                    .verticalScroll(rememberScrollState())
                    .padding(30.dp)
            ) {
                IconImage(
                    size = 100.dp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = signUpViewModel.userName,
                    singleLine = true,
                    shape = shapes.large,
                    isError = uiState is SignUpUiState.Error,
                    label = { Text(text = stringResource(R.string.text_field_user_name)) },
                    onValueChange = { signUpViewModel.onEvent(SignUpUiEvent.UpdateUserName(it)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorScheme.primary,
                        focusedTextColor = colorScheme.inverseOnSurface,
                        unfocusedTextColor = colorScheme.inverseOnSurface,
                        errorTextColor = colorScheme.inverseOnSurface,
                        focusedLabelColor = colorScheme.inverseOnSurface,
                        unfocusedLabelColor = colorScheme.inverseOnSurface,
                        unfocusedBorderColor = colorScheme.inverseOnSurface.copy(alpha = 0.5f),
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = signUpViewModel.userEmail,
                    singleLine = true,
                    shape = shapes.large,
                    isError = uiState is SignUpUiState.Error,
                    label = { Text(text = stringResource(R.string.text_field_user_email)) },
                    onValueChange = { signUpViewModel.onEvent(SignUpUiEvent.UpdateUserEmail(it)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorScheme.primary,
                        focusedTextColor = colorScheme.inverseOnSurface,
                        unfocusedTextColor = colorScheme.inverseOnSurface,
                        errorTextColor = colorScheme.inverseOnSurface,
                        focusedLabelColor = colorScheme.inverseOnSurface,
                        unfocusedLabelColor = colorScheme.inverseOnSurface,
                        unfocusedBorderColor = colorScheme.inverseOnSurface.copy(alpha = 0.5f),
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = signUpViewModel.userPassword,
                    singleLine = true,
                    shape = shapes.large,
                    isError = uiState is SignUpUiState.Error,
                    label = { Text(text = stringResource(R.string.text_field_user_password)) },
                    onValueChange = { signUpViewModel.onEvent(SignUpUiEvent.UpdateUserPassword(it)) },
                    visualTransformation = if (isUserPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { isUserPasswordVisible = !isUserPasswordVisible }) {
                            Icon(
                                imageVector = if (isUserPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = stringResource(R.string.description_icon_toggle_password_visibility)
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorScheme.primary,
                        focusedTextColor = colorScheme.inverseOnSurface,
                        unfocusedTextColor = colorScheme.inverseOnSurface,
                        errorTextColor = colorScheme.inverseOnSurface,
                        focusedLabelColor = colorScheme.inverseOnSurface,
                        unfocusedLabelColor = colorScheme.inverseOnSurface,
                        unfocusedBorderColor = colorScheme.inverseOnSurface.copy(alpha = 0.5f),
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = signUpViewModel.userPasswordConfirmation,
                    singleLine = true,
                    shape = shapes.large,
                    isError = uiState is SignUpUiState.Error,
                    label = { Text(text = stringResource(R.string.text_field_user_password)) },
                    onValueChange = { signUpViewModel.onEvent(SignUpUiEvent.UpdateUserPasswordConfirmation(it)) },
                    visualTransformation = if (isUserPasswordConfirmationVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    trailingIcon = {
                        IconButton(onClick = { isUserPasswordConfirmationVisible = !isUserPasswordConfirmationVisible }) {
                            Icon(
                                imageVector = if (isUserPasswordConfirmationVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = stringResource(R.string.description_icon_toggle_password_visibility)
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorScheme.primary,
                        focusedTextColor = colorScheme.inverseOnSurface,
                        unfocusedTextColor = colorScheme.inverseOnSurface,
                        errorTextColor = colorScheme.inverseOnSurface,
                        focusedLabelColor = colorScheme.inverseOnSurface,
                        unfocusedLabelColor = colorScheme.inverseOnSurface,
                        unfocusedBorderColor = colorScheme.inverseOnSurface.copy(alpha = 0.5f),
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        activity?.let {
                            signUpViewModel.onEvent(SignUpUiEvent.CreateUserWithEmailAndPassword(it))
                        }
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
                    Text(text = stringResource(R.string.text_button_sign_up))
                }

                when (val state = uiState) {
                    is SignUpUiState.Loading -> {
                        Spacer(modifier = Modifier.height(10.dp))
                        CircularProgressIndicator(
                            modifier = modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                    is SignUpUiState.Error -> {
                        Toast.makeText(
                            context,
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is SignUpUiState.Success -> {
                        Toast.makeText(
                            context,
                            stringResource(R.string.text_success_sign_up),
                            Toast.LENGTH_SHORT
                        ).show()
                        signUpViewModel.onEvent(SignUpUiEvent.ClearState)
                        handleLoginNavigation()
                    }
                    else -> {}
                }
            }
        }
    }
}
