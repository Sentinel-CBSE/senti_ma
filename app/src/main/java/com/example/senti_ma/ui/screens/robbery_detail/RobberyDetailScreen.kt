package com.example.senti_ma.ui.screens.robbery_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Composable for the robbery detail screen.
 */
@Composable
fun RobberyDetailScreen(
    modifier: Modifier = Modifier,
    robberyId: String
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Robbery Details: $robberyId")
    }
}
