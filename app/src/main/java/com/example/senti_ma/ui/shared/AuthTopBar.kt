package com.example.senti_ma.ui.shared

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.senti_ma.R

/**
 * Displays a top app bar with a back navigation icon.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthTopBar(
    title: String,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
){
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton (onClick = { onBackPressed() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = stringResource(R.string.description_icon_return_to_login)
                )
            }
        },
        modifier = modifier
    )
}
