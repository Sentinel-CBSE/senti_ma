package com.unal.senti_ma.ui.screens.signUp

import android.app.Activity
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.unal.senti_ma.R
import com.unal.senti_ma.ui.screens.signUp.components.SignUpContent
import com.unal.senti_ma.ui.screens.signUp.events.SignUpUiEvent
import com.unal.senti_ma.ui.screens.signUp.events.SignUpViewModelEvent

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    handleLoginNavigation: () -> Unit
) {
    val uiState by signUpViewModel.uiState.collectAsState()
    val formState by signUpViewModel.formState.collectAsState()

    val context = LocalContext.current
    val activity = context as? Activity

    val successMessage = stringResource(R.string.text_success_sign_up)

    LaunchedEffect(Unit) {
        signUpViewModel.viewModelEvent.collect { event ->
            when (event) {
                is SignUpViewModelEvent.Success -> {
                    Toast.makeText(
                        context,
                        successMessage,
                        Toast.LENGTH_SHORT
                    ).show()

                    signUpViewModel.onEvent(
                        SignUpUiEvent.ClearState
                    )

                    handleLoginNavigation()
                }

                is SignUpViewModelEvent.Error -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    SignUpContent(
        modifier = modifier,
        uiState = uiState,
        formState = formState,
        onEvent = signUpViewModel::onEvent,
        onBackPressed = {
            signUpViewModel.onEvent(
                SignUpUiEvent.ClearState
            )
            handleLoginNavigation()
        },
        onCreateUser = {
            activity?.let {
                signUpViewModel.onEvent(
                    SignUpUiEvent.CreateUserWithEmailAndPassword(it)
                )
            }
        }
    )
}
