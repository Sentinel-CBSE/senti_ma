package com.unal.senti_ma.ui.screens.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.unal.senti_ma.R
import com.unal.senti_ma.domain.model.User
import com.unal.senti_ma.ui.screens.profile.events.ProfileUiEvent
import com.unal.senti_ma.ui.settings.ThemeViewModel

@Composable
fun ProfileScreen(
    user: User,
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel()
) {
    val profileUiState by profileViewModel.uiState.collectAsState()
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(profileUiState) {
        val state = profileUiState
        if (state is ProfileUiState.Error) {
            Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        isDarkTheme?.let { darkTheme ->
            TextButton(
                onClick = { themeViewModel.setDarkTheme(!darkTheme) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.DarkMode,
                    contentDescription = stringResource(R.string.description_icon_theme)
                )
                Text(
                    text = stringResource(R.string.text_dark_theme),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                )
                Switch(
                    checked = darkTheme,
                    onCheckedChange = { themeViewModel.setDarkTheme(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                    )
                )
            }
        } ?: CircularProgressIndicator()

        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        if (profileUiState is ProfileUiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            TextButton(
                onClick = { profileViewModel.onEvent(ProfileUiEvent.SignOut) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = stringResource(R.string.description_icon_sign_out)
                )
                Text(
                    text = stringResource(R.string.text_sign_out),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                )
            }
        }
    }
}
