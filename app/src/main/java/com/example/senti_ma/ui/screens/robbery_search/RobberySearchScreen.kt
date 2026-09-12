package com.example.senti_ma.ui.screens.robbery_search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Composable for the robbery search screen.
 */
@Composable
fun RobberySearchScreen(
    modifier: Modifier = Modifier,
    onRobberyClick: (String) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // TODO: tabla/lista de robos; al tocar una fila -> onRobberyClick(robbery.id)
        Text("Robbery Search (pending implementation)")
    }
}
