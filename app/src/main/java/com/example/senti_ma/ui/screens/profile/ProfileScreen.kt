package com.example.senti_ma.ui.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.senti_ma.domain.model.User

/**
 * Composable for the profile screen.
 */
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onSignOut: () -> Unit,
    user: User
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // TODO: datos reales del perfil + botón de sign out
        Text("profile de ${user.displayName ?: user.email ?: "usuario"}")
    }
}
